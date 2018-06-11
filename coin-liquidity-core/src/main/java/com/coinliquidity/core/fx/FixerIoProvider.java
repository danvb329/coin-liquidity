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
    private LocalDate lastLoadDate;
    private FxRates fxRates;

    public FixerIoProvider(final HttpClient httpClient, final String baseCcy, final String accessKey) {
        this.httpClient = httpClient;
        this.baseCcy = baseCcy;
        this.url = "http://data.fixer.io/api/latest?access_key=" + accessKey;
        this.refresh();
    }

    @Override
    public FxRates getRates() {
        return fxRates;
    }

    @Override
    public void refresh() {
        if (lastLoadDate != null && lastLoadDate.equals(LocalDate.now())) {
            LOGGER.info("Already have FX rates for " + lastLoadDate);
            return;
        }
        try {
            final FxRates tempRates = new FxRates(baseCcy, Instant.now());

            final JsonNode tree = httpClient.get(url);
            final String dateStr = tree.get("date").asText();
            final String jsonBase = tree.get("base").asText();
            final LocalDate currentDate = LocalDate.parse(dateStr);

            BigDecimal factor = BigDecimal.ONE;
            if (!baseCcy.equals(jsonBase)) {
                factor = new BigDecimal(tree.get("rates").get(baseCcy).asText());
            }

            if (!currentDate.equals(dataDate)) {
                LOGGER.info("Rates updated for {}", currentDate);
                final Iterator<Map.Entry<String, JsonNode>> it = tree.get("rates").fields();
                while (it.hasNext()) {
                    final Map.Entry<String, JsonNode> entry = it.next();
                    if (!baseCcy.equals(entry.getKey())) {
                        final BigDecimal rate = new BigDecimal(entry.getValue().asText());
                        tempRates.putRate(entry.getKey(), rate.divide(factor, 8, BigDecimal.ROUND_HALF_EVEN));
                    }
                }

                fxRates = tempRates;
                dataDate = currentDate;
                lastLoadDate = LocalDate.now();
            }
        } catch (final Exception e) {
            LOGGER.warn("Could not refresh, current rates as of {}", dataDate);
        }
    }

    LocalDate getDataDate() {
        return dataDate;
    }
}
