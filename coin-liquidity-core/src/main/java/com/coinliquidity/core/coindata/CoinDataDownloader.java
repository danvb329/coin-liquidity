package com.coinliquidity.core.coindata;

import com.coinliquidity.core.model.CoinDatum;
import com.coinliquidity.core.util.HttpClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class CoinDataDownloader {

    private static final BigDecimal MIN_MARKET_CAP = BigDecimal.valueOf(10_000_000);

    private static final Map<String, String> ID_MAP = Maps.newHashMap();
    private static final Map<String, String> NAME_MAP = Maps.newHashMap();

    static {
        ID_MAP.put("bitcoin-cash", "bcash");
        NAME_MAP.put("Bitcoin Cash", "Bcash");
    }

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
            String id = coinNode.get("id").asText();
            String name = coinNode.get("name").asText();
            final String symbol = coinNode.get("symbol").asText();

            id = ID_MAP.getOrDefault(id, id);
            name = NAME_MAP.getOrDefault(name, name);

            final BigDecimal marketCap = toDecimal(coinNode, "market_cap_usd");

            if (marketCap != null && marketCap.compareTo(MIN_MARKET_CAP) >= 0) {
                final CoinDatum coinDatum = new CoinDatum();
                coinDatum.setRunDate(now);
                coinDatum.setId(id);
                coinDatum.setName(name);
                coinDatum.setSymbol(symbol);
                coinDatum.setPriceUsd(toDecimal(coinNode, "price_usd"));
                coinDatum.setPriceBtc(toDecimal(coinNode, "price_btc"));
                coinDatum.setVolume24hUsd(toDecimal(coinNode, "24h_volume_usd"));
                coinDatum.setMarketCapUsd(marketCap);
                coinDatum.setAvailableSupply(toDecimal(coinNode, "available_supply"));
                coinDatum.setTotalSupply(toDecimal(coinNode, "total_supply"));
                coinDatum.setMaxSupply(toDecimal(coinNode, "max_supply"));
                coinDatum.setLastUpdated(Instant.ofEpochSecond(coinNode.get("last_updated").asLong()));

                coinData.add(coinDatum);
            }
        }
        return coinData;
    }

    private BigDecimal toDecimal(final JsonNode node, final String field) {
        final JsonNode value = node.get(field);
        return value == null || value.isNull() ? null : new BigDecimal(value.asText());
    }

}
