package com.coinliquidity.core.analyzer;

import com.coinliquidity.core.model.OrderBook;
import com.google.common.collect.Sets;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

public class CloseBidAskAnalyzer implements Analyzer {

    public static final Set<Integer> PERCENTAGES = Sets.newHashSet(1, 2, 3, 5, 10, 20);

    private final BigDecimal maxAsk;
    private final BigDecimal minBid;

    public CloseBidAskAnalyzer(final BigDecimal price, final int percent) {
        final BigDecimal fraction = new BigDecimal(percent)
                .divide(new BigDecimal(100), 2, BigDecimal.ROUND_UNNECESSARY);
        this.maxAsk= price.multiply(BigDecimal.ONE.add(fraction));
        this.minBid = price.multiply(BigDecimal.ONE.subtract(fraction));
    }

    private BigDecimal totalBids = BigDecimal.ZERO;
    private BigDecimal totalAsks = BigDecimal.ZERO;

    @Override
    public void analyze(final OrderBook orderBook) {
        orderBook.getAsks()
                .forEach(order -> {
                    if (order.getPrice().compareTo(maxAsk) <= 0) {
                        totalAsks = totalAsks.add(order.getUnits());
                    }
                });
        orderBook.getBids().forEach(order -> {
            if (order.getPrice().compareTo(minBid) >= 0) {
                totalBids = totalBids.add(order.getAmount());
            }
        });
    }

    public BigDecimal getTotalBids() {
        return totalBids.setScale(2, RoundingMode.DOWN);
    }

    public BigDecimal getTotalAsks() {
        return totalAsks.setScale(0, RoundingMode.DOWN);
    }
}
