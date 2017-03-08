package com.coinliquidity.core;

/**
 * Utility to test exchange config
 */
public class OrderBookDownloaderMain {

    public static void main(String[] args) {
        final ExchangeConfig config = new ExchangeConfig("exchange-test.json");

        config.loadExchanges().getExchangeList().forEach(exchange -> {
                    OrderBookDownloader obd = new OrderBookDownloader(exchange);
                    obd.run();
                    System.out.println(obd.getOrderBooks());
                });

    }
}
