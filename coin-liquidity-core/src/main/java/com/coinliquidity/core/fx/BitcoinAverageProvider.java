package com.coinliquidity.core.fx;

import com.coinliquidity.core.util.HttpClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

import static com.coinliquidity.core.model.CurrencyPair.BTC;

public class BitcoinAverageProvider implements FxProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(BitcoinAverageProvider.class);

    private final HttpClient httpClient;
    private final String baseCcy;
    private final String url;

    public BitcoinAverageProvider(final HttpClient httpClient, final String baseCcy) {
        this.httpClient = httpClient;
        this.baseCcy = baseCcy;
        this.url = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTC" + baseCcy;
    }

    @Override
    public FxRates getRates() {
        final JsonNode tree = httpClient.get(url);
        final BigDecimal price = new BigDecimal(tree.get("last").asText());
        final Instant updateTime = parseUpdateTime(tree);
        return new FxRates(baseCcy, updateTime).putInverseRate(BTC, price);
    }

    private Instant parseUpdateTime(final JsonNode tree) {
        try {
            return Instant.ofEpochSecond(Long.parseLong(tree.get("timestamp").asText()));
        } catch (final Exception e) {
            LOGGER.warn("Could not parse timestamp", e);
            return null;
        }
    }
}
