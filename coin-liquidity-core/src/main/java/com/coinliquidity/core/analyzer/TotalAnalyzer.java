package com.coinliquidity.core.analyzer;

import com.coinliquidity.core.model.OrderBook;

import java.math.BigDecimal;

public class TotalAnalyzer implements Analyzer {

    private BigDecimal totalBids = BigDecimal.ZERO;
    private BigDecimal totalAsks = BigDecimal.ZERO;

    @Override
    public void analyze(final OrderBook orderBook) {
        orderBook.getAsks().forEach(order -> totalAsks = totalAsks.add(order.getUnits()));
        orderBook.getBids().forEach(order -> totalBids = totalBids.add(order.getAmount()));
    }
}
