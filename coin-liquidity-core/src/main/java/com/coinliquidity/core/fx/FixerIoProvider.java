package com.coinliquidity.core.fx;

import com.coinliquidity.core.util.HttpClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;

public class FixerIoProvider implements FxProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(FixerIoProvider.class);

    private final HttpClient httpClient;

    private final String baseCcy;
    private final String url;

    private LocalDate dataDate;
    private FxRates fxRates;

    public FixerIoProvider(final HttpClient httpClient, final String baseCcy) {
        this.httpClient = httpClient;
        this.baseCcy = baseCcy;
        this.url = "https://api.fixer.io/latest?base=" + baseCcy;
        this.refresh();
    }

    @Override
    public FxRates getRates() {
        return fxRates;
    }

    @Override
    public void refresh() {
        try {
            final FxRates tempRates = new FxRates(baseCcy, Instant.now());

            final JsonNode tree = httpClient.get(url);
            final String dateStr = tree.get("date").asText();
            final LocalDate currentDate = LocalDate.parse(dateStr);

            if (!currentDate.equals(dataDate)) {
                LOGGER.info("Rates updated for {}", currentDate);
                final Iterator<Map.Entry<String, JsonNode>> it = tree.get("rates").fields();
                while (it.hasNext()) {
                    final Map.Entry<String, JsonNode> entry = it.next();
                    tempRates.putRate(entry.getKey(), new BigDecimal(entry.getValue().asText()));
                }

                fxRates = tempRates;
                dataDate = currentDate;
            }
        } catch (final Exception e) {
            LOGGER.warn("Could not refresh, current rates as of {}", dataDate);
        }
    }

    LocalDate getDataDate() {
        return dataDate;
    }
}
