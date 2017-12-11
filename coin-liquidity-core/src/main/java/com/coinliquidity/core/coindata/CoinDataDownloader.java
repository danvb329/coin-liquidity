package com.coinliquidity.core.coindata;

import com.coinliquidity.core.model.CoinDatum;
import com.coinliquidity.core.util.HttpClient;
import com.coinliquidity.core.util.ResourceUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;

public class CoinDataDownloader {

    private static final BigDecimal MIN_MARKET_CAP = BigDecimal.valueOf(10_000_000);

    private final JsonNode overrides;

    private final HttpClient httpClient;
    private final String url;

    public CoinDataDownloader(final String url,
                              final HttpClient httpClient) {
        this.httpClient = httpClient;
        this.url = url;
        this.overrides = ResourceUtils.json("coin_data_overrides.json");
    }

    public List<CoinDatum> downloadData(final Instant now) {
        final JsonNode tree = httpClient.get(url);
        final List<CoinDatum> coinData = Lists.newArrayList();

        for (final JsonNode coinNode : tree) {
            final JsonNode override = overrides.path(coinNode.get("id").asText());
            final BigDecimal marketCap = toDecimal(coinNode, override,"market_cap_usd");

            if (marketCap != null && marketCap.compareTo(MIN_MARKET_CAP) >= 0) {
                final CoinDatum coinDatum = new CoinDatum();
                coinDatum.setRunDate(now);
                coinDatum.setId(getString(coinNode, override, "id"));
                coinDatum.setName(getString(coinNode, override, "name"));
                coinDatum.setSymbol(getString(coinNode, override, "symbol"));
                coinDatum.setPriceUsd(toDecimal(coinNode, override,"price_usd"));
                coinDatum.setPriceBtc(toDecimal(coinNode, override,"price_btc"));
                coinDatum.setVolume24hUsd(toDecimal(coinNode, override,"24h_volume_usd"));
                coinDatum.setMarketCapUsd(marketCap);
                coinDatum.setAvailableSupply(toDecimal(coinNode, override,"available_supply"));
                coinDatum.setTotalSupply(toDecimal(coinNode, override,"total_supply"));
                coinDatum.setMaxSupply(toDecimal(coinNode, override,"max_supply"));
                coinDatum.setLastUpdated(Instant.ofEpochSecond(coinNode.get("last_updated").asLong()));

                coinData.add(coinDatum);
            }
        }
        return coinData;
    }

    private BigDecimal toDecimal(final JsonNode node, final JsonNode override, final String field) {
        final String value = getString(node, override, field);
        return value == null ? null : new BigDecimal(value).setScale(0, RoundingMode.DOWN);
    }

    private String getString(final JsonNode node, final JsonNode override, final String field) {
        JsonNode value = override.path(field);
        if (value.isMissingNode() || value.isNull()) {
            value = node.path(field);
        }
        return value.isMissingNode() || value.isNull() ? null : value.asText();
    }

}
