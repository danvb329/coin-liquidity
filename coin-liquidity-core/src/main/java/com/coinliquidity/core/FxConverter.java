package com.coinliquidity.core;

import com.coinliquidity.core.util.HttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import static com.coinliquidity.core.model.CurrencyPair.BTC;

public class FxConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(FxConverter.class);

    private static final String URL = "http://api.fixer.io/latest?base=";

    private final String baseCcy;
    private final Map<String, BigDecimal> rates;

    public FxConverter(final String baseCcy) {
        this.baseCcy = baseCcy;
        this.rates = fetchRates();
    }

    private Map<String, BigDecimal> fetchRates() {
        final Map<String, BigDecimal> rates = new TreeMap<>();

        JsonNode tree = HttpUtil.get(URL + baseCcy);
        final Iterator<Map.Entry<String, JsonNode>> it = tree.get("rates").fields();
        while (it.hasNext()) {
            final Map.Entry<String, JsonNode> entry = it.next();
            rates.put(entry.getKey(), new BigDecimal(entry.getValue().asText()));
        }

        // add BTC rate
        tree = HttpUtil.get("https://api.bitcoinaverage.com/ticker/USD/");
        rates.put(BTC, inverse(new BigDecimal(tree.get("last").asText())));

        LOGGER.info("FX rates: {}", rates);

        return rates;
    }

    public BigDecimal getRate(final String currency) {
        if (baseCcy.equals(currency)) {
            return BigDecimal.ONE;
        }

        BigDecimal rate = rates.get(currency);

        if (rate == null) {
            throw new RuntimeException("No FX rate for " + currency);
        }

        return rate;
    }

    public String getBaseCcy() {
        return baseCcy;
    }

    private BigDecimal inverse(final BigDecimal rate) {
        return BigDecimal.ONE.divide(rate, 5, BigDecimal.ROUND_HALF_UP);
    }
}
