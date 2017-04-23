package com.coinliquidity.core.fx;

import com.coinliquidity.core.util.HttpClient;
import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Iterator;
import java.util.Map;

import static com.coinliquidity.core.model.CurrencyPair.USD;

public class PoloniexProvider implements FxProvider {

    private static final String URL = "https://poloniex.com/public?command=returnTicker";
    private static final String USDT = "USDT_";

    private final HttpClient httpClient;

    public PoloniexProvider(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public FxRates getRates() {
        final FxRates fxRates = new FxRates(USD, Instant.now());
        final JsonNode tree = httpClient.get(URL);

        for (Iterator<Map.Entry<String, JsonNode>> it = tree.fields(); it.hasNext(); ) {
            final Map.Entry<String, JsonNode> entry = it.next();
            if (entry.getKey().startsWith(USDT)) {
                final String ccy = entry.getKey().replaceAll(USDT, "");
                final BigDecimal price = new BigDecimal(entry.getValue().get("last").asText());
                fxRates.putInverseRate(ccy, price);
            }
        }

        return fxRates;
    }


}
