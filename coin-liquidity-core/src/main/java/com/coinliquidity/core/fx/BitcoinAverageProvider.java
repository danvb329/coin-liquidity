package com.coinliquidity.core.fx;

import com.coinliquidity.core.util.HttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static com.coinliquidity.core.model.CurrencyPair.BTC;

public class BitcoinAverageProvider implements FxProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(BitcoinAverageProvider.class);

    // Wed, 05 Apr 2017 13:28:25 -0000
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss XXXX");

    private final String baseCcy;
    private final String url;

    public BitcoinAverageProvider(final String baseCcy) {
        this.baseCcy = baseCcy;
        this.url = "https://api.bitcoinaverage.com/ticker/" + baseCcy;
    }

    @Override
    public FxRates getRates() {
        final JsonNode tree = HttpUtil.get(url);
        final BigDecimal rate = new BigDecimal(tree.get("last").asText());
        final BigDecimal inverseRate = inverse(rate);
        final Instant updateTime = parseUpdateTime(tree);
        return new FxRates(baseCcy, updateTime, Collections.singletonMap(BTC, inverseRate));
    }

    private BigDecimal inverse(final BigDecimal rate) {
        return BigDecimal.ONE.divide(rate, 10, BigDecimal.ROUND_HALF_UP);
    }

    private Instant parseUpdateTime(final JsonNode tree) {
        try {
            return Instant.from(FORMATTER.parse(tree.get("timestamp").asText()));
        } catch (final Exception e) {
            LOGGER.warn("Could not parse timestamp", e);
            return null;
        }
    }
}
