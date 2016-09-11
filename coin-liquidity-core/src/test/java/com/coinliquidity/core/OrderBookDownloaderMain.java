package com.coinliquidity.core;

/**
 * Utility to test exchange config
 */
public class OrderBookDownloaderMain {

    public static void main(String[] args) {
        final ExchangeConfig config = new ExchangeConfig("exchange-test.json");

        config.loadExchanges().getExchangeList()
                .forEach(exchange -> new OrderBookDownloader(exchange).run());

    }
}
