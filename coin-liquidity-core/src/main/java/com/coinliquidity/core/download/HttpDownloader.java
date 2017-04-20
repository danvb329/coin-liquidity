package com.coinliquidity.core.download;

import com.coinliquidity.core.model.CurrencyPair;
import com.coinliquidity.core.model.Exchange;
import com.coinliquidity.core.model.OrderBook;
import com.coinliquidity.core.util.HttpClient;
import com.fasterxml.jackson.databind.JsonNode;

public class HttpDownloader implements Downloader {

    private final HttpClient httpClient;

    public HttpDownloader(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public OrderBook download(final Exchange exchange, final CurrencyPair currencyPair) {
        final String url = getUrl(exchange, currencyPair);
        final JsonNode tree = httpClient.get(url);
        return exchange.getParserType().getParser().parse(exchange.getName(), currencyPair.normalize(), tree);
    }

    @Override
    public String getUrl(final Exchange exchange, final CurrencyPair currencyPair) {
        final String base = currencyPair.getBaseCurrency();
        final String quote = currencyPair.getQuoteCurrency();
        return exchange.getUrl()
                .replaceAll("<BASE>", base.toUpperCase())
                .replaceAll("<base>", base.toLowerCase())
                .replaceAll("<QUOTE>", quote.toUpperCase())
                .replaceAll("<quote>", quote.toLowerCase())
                .replaceAll("<MAX_DEPTH>", String.valueOf(exchange.getMaxDepth()));
    }
}
