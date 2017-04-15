package com.coinliquidity.core;

import com.coinliquidity.core.analyzer.BidAskAnalyzer;
import com.coinliquidity.core.model.OrderBook;

import static com.coinliquidity.core.analyzer.BidAskAnalyzer.PERCENTAGES;

/**
 * Utility to test exchange config
 */
public class OrderBookDownloaderMain {

    public static void main(String[] args) {
        final ExchangeConfig config = new ExchangeConfig("exchange-test.json");

        config.loadExchanges().getExchangeList().forEach(exchange -> {
                    OrderBookDownloader obd = new OrderBookDownloader(exchange);
                    obd.run();

                    final OrderBook orderBook = obd.getOrderBooks().get(0);

                    PERCENTAGES.forEach(percent -> {
                        final BidAskAnalyzer analyzer = new BidAskAnalyzer(percent);
                        analyzer.analyze(orderBook);
                        System.out.println(percent +
                                "\tBids:\t" + analyzer.getTotalBids() +
                                "\tAsks:\t" + analyzer.getTotalAsks() +
                                "\tBids USD:\t" + analyzer.getTotalBidsUsd() +
                                "\tAsks USD:\t" + analyzer.getTotalAsksUsd());
                    });

                });

    }
}
