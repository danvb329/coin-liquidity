package com.coinliquidity.core.fx;

import com.coinliquidity.core.util.HttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Iterator;
import java.util.Map;

import static com.coinliquidity.core.model.CurrencyPair.USD;
import static com.coinliquidity.core.util.DecimalUtils.inverseRate;

public class PoloniexProvider implements FxProvider {

    private static final String URL = "https://poloniex.com/public?command=returnTicker";
    private static final String USDT = "USDT_";

    @Override
    public FxRates getRates() {
        final JsonNode tree = HttpUtil.get(URL);

        final Map<String, BigDecimal> rates = Maps.newHashMap();
        final Map<String, BigDecimal> inverseRates = Maps.newHashMap();

        for (Iterator<Map.Entry<String, JsonNode>> it = tree.fields(); it.hasNext(); ) {
            final Map.Entry<String, JsonNode> entry = it.next();
            if (entry.getKey().startsWith(USDT)) {
                final String ccy = entry.getKey().replaceAll(USDT, "");
                final BigDecimal price = new BigDecimal(entry.getValue().get("last").asText());
                final BigDecimal rate = inverseRate(price);

                rates.put(ccy, rate);
                inverseRates.put(ccy, price);
            }
        }

        return new FxRates(USD, Instant.now(), rates, inverseRates);
    }


}
