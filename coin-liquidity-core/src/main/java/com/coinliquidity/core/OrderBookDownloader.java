package com.coinliquidity.core;

import com.coinliquidity.core.download.HttpDownloader;
import com.coinliquidity.core.model.CurrencyPair;
import com.coinliquidity.core.model.DownloadStatus;
import com.coinliquidity.core.model.Exchange;
import com.coinliquidity.core.model.OrderBook;
import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OrderBookDownloader implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderBookDownloader.class);

    private final Exchange exchange;
    private final HttpDownloader httpDownloader;

    private final List<OrderBook> orderBooks;
    private final List<DownloadStatus> downloadStatuses;

    public OrderBookDownloader(final Exchange exchange, final HttpDownloader httpDownloader) {
        this.exchange = exchange;
        this.httpDownloader = httpDownloader;
        this.orderBooks = new ArrayList<>();
        this.downloadStatuses = new ArrayList<>();
    }

    public void run() {
        int maxRate = (1000 / exchange.getRateLimit()) + 1;

        for (final CurrencyPair currencyPair : exchange.getCurrencies()) {
            // skip disabled currencies
            if (currencyPair.isDisabled()) {
                continue;
            }

            final Stopwatch stopwatch = Stopwatch.createStarted();
            final String orderBookUrl = httpDownloader.getUrl(exchange, currencyPair);
            final DownloadStatus downloadStatus = initDownloadStatus(currencyPair, orderBookUrl);

            try {
                final OrderBook orderBook = httpDownloader.download(exchange, currencyPair);
                orderBooks.add(orderBook);

                downloadStatus.setStatus(DownloadStatus.OK);
                downloadStatus.setTotalAsks(orderBook.getAsks().size());
                downloadStatus.setTotalBids(orderBook.getBids().size());
            } catch (final Exception e) {
                LOGGER.error("Error processing {}_{}, URL {}", exchange.getName(), currencyPair, orderBookUrl, e);

                downloadStatus.setStatus(DownloadStatus.ERROR);
                downloadStatus.setLastErrorMessage(e.getMessage());
            }

            LOGGER.debug("Download for {} {} took {}", exchange, currencyPair, stopwatch.stop());
            final long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);

            downloadStatus.setTimeElapsed(elapsed);

            if (elapsed < maxRate) {
                LOGGER.debug("Too fast, sleeping for {} ms", maxRate - elapsed);
                sleep(maxRate - elapsed);
            }
        }
    }

    private DownloadStatus initDownloadStatus(final CurrencyPair currencyPair, final String orderBookUrl) {
        final DownloadStatus downloadStatus = new DownloadStatus();
        downloadStatus.setCurrencyPair(currencyPair);
        downloadStatus.setExchange(exchange);
        downloadStatus.setUpdateTime(LocalDateTime.now());
        downloadStatus.setOrderBookUrl(orderBookUrl);
        downloadStatuses.add(downloadStatus);
        return downloadStatus;
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

    public List<DownloadStatus> getDownloadStatuses() {
        return downloadStatuses;
    }
}
