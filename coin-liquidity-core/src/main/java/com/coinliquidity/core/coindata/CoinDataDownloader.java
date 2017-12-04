package com.coinliquidity.core.coindata;

import com.coinliquidity.core.model.CoinDatum;
import com.coinliquidity.core.util.HttpClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

public class CoinDataDownloader {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoinDataDownloader.class);

    private final HttpClient httpClient;
    private final String url;

    public CoinDataDownloader(final String url,
                              final HttpClient httpClient) {
        this.httpClient = httpClient;
        this.url = url;
    }

    public List<CoinDatum> downloadData(final Instant now) {
        final JsonNode tree = httpClient.get(url);
        final List<CoinDatum> coinData = Lists.newArrayList();
        final Set<String> symbols = Sets.newHashSet();
        for (final JsonNode coinNode : tree) {
            final String symbol = coinNode.get("symbol").asText();

            // to prevent duplicates
            if (!symbols.contains(symbol)) {
                final CoinDatum coinDatum = new CoinDatum();
                coinDatum.setRunDate(now);
                coinDatum.setSymbol(symbol);
                coinDatum.setPriceUsd(toDecimal(coinNode, "price_usd"));
                coinDatum.setPriceBtc(toDecimal(coinNode, "price_btc"));
                coinDatum.setVolume24hUsd(toDecimal(coinNode, "24h_volume_usd"));
                coinDatum.setMarketCapUsd(toDecimal(coinNode, "market_cap_usd"));
                coinDatum.setAvailableSupply(toDecimal(coinNode, "available_supply"));
                coinDatum.setTotalSupply(toDecimal(coinNode, "total_supply"));
                coinDatum.setMaxSupply(toDecimal(coinNode, "max_supply"));
                coinDatum.setLastUpdated(Instant.ofEpochSecond(coinNode.get("last_updated").asLong()));

                coinData.add(coinDatum);
                symbols.add(symbol);
            } else {
                //LOGGER.warn("Duplicate symbol: {}", symbol);
            }
        }
        return coinData;
    }

    private BigDecimal toDecimal(final JsonNode node, final String field) {
        final JsonNode value = node.get(field);
        return value == null || value.isNull() ? null : new BigDecimal(value.asText());
    }

}
