package com.coinliquidity.web;

import com.coinliquidity.core.model.CurrencyPair;
import com.coinliquidity.core.model.DownloadStatus;
import com.coinliquidity.core.model.Exchange;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class StatusCacheTest {

    @Test
    public void process() {

        StatusCache statusCache = new StatusCache();

        List<DownloadStatus> downloadStatuses = Lists.newArrayList();
        downloadStatuses.add(downloadStatus("gdax.com", new CurrencyPair("BTC", "USD")));
        downloadStatuses.add(downloadStatus("gdax.com", new CurrencyPair("ETH", "USD")));
        statusCache.process(downloadStatuses.stream());

        assertEquals(2, statusCache.getDownloadStatuses().size());

        List<DownloadStatus> downloadStatuses2 = Lists.newArrayList();
        downloadStatuses2.add(downloadStatus("gdax.com", new CurrencyPair("BTC", "USD")));
        statusCache.process(downloadStatuses2.stream());

        assertEquals(1, statusCache.getDownloadStatuses().size());
    }

    private DownloadStatus downloadStatus(String exchangeName, CurrencyPair currencyPair) {
        final Exchange exchange = new Exchange();
        exchange.setName(exchangeName);

        final DownloadStatus downloadStatus = new DownloadStatus();
        downloadStatus.setExchange(exchange);
        downloadStatus.setCurrencyPair(currencyPair);
        return downloadStatus;
    }
}