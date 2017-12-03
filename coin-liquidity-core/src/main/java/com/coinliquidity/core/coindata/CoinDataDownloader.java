package com.coinliquidity.core.coindata;

import com.coinliquidity.core.model.CoinDatum;
import com.coinliquidity.core.util.HttpClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class CoinDataDownloader {

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
        for (final JsonNode coinNode : tree) {
            final CoinDatum coinDatum = new CoinDatum();
            coinDatum.setRunDate(now);
            coinDatum.setSymbol(coinNode.get("symbol").asText());
            coinDatum.setPriceUsd(toDecimal(coinNode, "price_usd"));
            coinDatum.setPriceBtc(toDecimal(coinNode, "price_btc"));
            coinDatum.setVolume24hUsd(toDecimal(coinNode, "24h_volume_usd"));
            coinDatum.setMarketCapUsd(toDecimal(coinNode, "market_cap_usd"));
            coinDatum.setAvailableSupply(toDecimal(coinNode, "available_supply"));
            coinDatum.setTotalSupply(toDecimal(coinNode, "total_supply"));
            coinDatum.setMaxSupply(toDecimal(coinNode, "max_supply"));
            coinDatum.setLastUpdated(Instant.ofEpochSecond(coinNode.get("last_updated").asLong()));

            coinData.add(coinDatum);
        }
        return coinData;
    }

    private BigDecimal toDecimal(final JsonNode node, final String field) {
        final JsonNode value = node.get(field);
        return value == null || value.isNull() ? null : new BigDecimal(value.asText());
    }

}
