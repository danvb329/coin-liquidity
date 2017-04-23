package com.coinliquidity.core.fx;

import com.coinliquidity.core.model.CurrencyPair;
import com.coinliquidity.core.util.HttpClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;

import static com.coinliquidity.core.util.DecimalUtils.inverseRate;

/**
 * For getting USDT rate - https://www.kraken.com/help/api#get-ticker-info
 */
public class KrakenProvider implements FxProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(KrakenProvider.class);
    private static final String PAIR = "USDTZUSD";
    static final String USDT = "USDT";
    private static final String URL = "https://api.kraken.com/0/public/Ticker?pair=" + PAIR;

    private final HttpClient httpClient;

    private FxRates fxRates;

    public KrakenProvider(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public FxRates getRates() {
        return fxRates;
    }

    @Override
    public void refresh() {
        try {
            final JsonNode tree = httpClient.get(URL);
            final BigDecimal price = new BigDecimal(tree.findPath(PAIR).findPath("c").get(0).asText());
            final BigDecimal rate = inverseRate(price);
            fxRates = new FxRates(CurrencyPair.USD,
                    Instant.now(),
                    Collections.singletonMap(USDT, rate),
                    Collections.singletonMap(USDT, price));
        } catch (final Exception e) {
            LOGGER.warn("Could not refresh, current rates as of {}", fxRates.getUpdateTime());
        }
    }
}
