package com.coinliquidity.core;

import com.coinliquidity.core.model.CurrencyPair;
import com.coinliquidity.core.model.Exchange;
import com.coinliquidity.core.model.OrderBook;
import com.coinliquidity.core.util.HttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OrderBookDownloader implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderBookDownloader.class);

    private final Exchange exchange;

    private final List<OrderBook> orderBooks;

    public OrderBookDownloader(final Exchange exchange) {
        this.exchange = exchange;
        this.orderBooks = new ArrayList<>();
    }

    public void run() {
        int maxRate = (1000 / exchange.getRateLimit()) + 1;

        for (final CurrencyPair currencyPair : exchange.getCurrencyPairs()) {
            // skip disabled currencies
            if (currencyPair.isDisabled()) {
                continue;
            }
            final Stopwatch stopwatch = Stopwatch.createStarted();
            downloadOrderBook(currencyPair);
            LOGGER.info("Download for {} {} took {}", exchange, currencyPair, stopwatch.stop());

            final long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);

            if (elapsed < maxRate) {
                LOGGER.debug("Too fast, sleeping for {} ms", maxRate - elapsed);
                sleep(maxRate - elapsed);
            }

        }
    }

    private void downloadOrderBook(final CurrencyPair currencyPair) {
        try {
            final String base = currencyPair.getBaseCurrency();
            final String quote = currencyPair.getQuoteCurrency();
            final String ccyUrl = exchange.getUrl()
                    .replaceAll("<BASE>", base.toUpperCase())
                    .replaceAll("<base>", base.toLowerCase())
                    .replaceAll("<QUOTE>", quote.toUpperCase())
                    .replaceAll("<quote>", quote.toLowerCase());
            final JsonNode tree = HttpUtil.get(ccyUrl);
            final OrderBook orderBook = exchange.getParser().parse(exchange.getName(), currencyPair.normalize(), tree);
            orderBooks.add(orderBook);
        } catch (Exception e) {
            LOGGER.error("Error processing {}_{}", exchange.getName(), currencyPair);
        }
    }

    private void sleep(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.warn("Sleeping interrupted");
        }
    }

    public List<OrderBook> getOrderBooks() {
        return orderBooks;
    }
}
