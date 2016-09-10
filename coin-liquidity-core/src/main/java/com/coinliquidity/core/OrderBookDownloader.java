package com.coinliquidity.core;

import com.coinliquidity.core.model.CurrencyPair;
import com.coinliquidity.core.model.Exchange;
import com.coinliquidity.core.model.OrderBook;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class OrderBookDownloader implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderBookDownloader.class);
    private static final int DOWNLOAD_DELAY = 1000;

    private final Exchange exchange;
    private final ObjectMapper mapper;

    private List<OrderBook> orderBooks = new ArrayList<>();

    public OrderBookDownloader(final Exchange exchange,
                               final ObjectMapper mapper) {
        this.exchange = exchange;
        this.mapper = mapper;
    }

    public void run() {
        orderBooks.clear();

        final Client client = ClientBuilder.newClient();

        int count = 0;
        for (final CurrencyPair currencyPair : exchange.getCurrencyPairs()) {
            downloadOrderBook(client, currencyPair);
            if (++count < exchange.getCurrencyPairs().size()) {
                try {
                    Thread.sleep(DOWNLOAD_DELAY);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void downloadOrderBook(final Client client, final CurrencyPair currencyPair) {
        try {
            final String base = currencyPair.getBaseCurrency();
            final String quote = currencyPair.getQuoteCurrency();
            final Stopwatch stopwatch = Stopwatch.createStarted();
            final String ccyUrl = exchange.getUrl()
                    .replaceAll("<BASE>", base.toUpperCase())
                    .replaceAll("<base>", base.toLowerCase())
                    .replaceAll("<QUOTE>", quote.toUpperCase())
                    .replaceAll("<quote>", quote.toLowerCase());
            final WebTarget target = client.target(ccyUrl);
            final Response response = target.request().get();
            final JsonNode tree = mapper.readTree(response.readEntity(InputStream.class));
            final OrderBook orderBook = exchange.getParser().parse(exchange.getName(), currencyPair.normalize(), tree);
            orderBooks.add(orderBook);
            LOGGER.info("Download for {} took {}", orderBook.getName(), stopwatch.stop());
        } catch (Exception e) {
            LOGGER.error("Error processing {}_{}", exchange.getName(), currencyPair);
        }
    }

    public List<OrderBook> getOrderBooks() {
        return orderBooks;
    }
}
