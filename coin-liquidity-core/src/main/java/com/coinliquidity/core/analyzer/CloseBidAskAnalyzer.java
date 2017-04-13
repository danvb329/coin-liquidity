package com.coinliquidity.core.analyzer;

import com.coinliquidity.core.model.OrderBook;
import com.google.common.collect.Sets;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

import static com.coinliquidity.core.util.DecimalUtils.avgPrice;
import static com.coinliquidity.core.util.DecimalUtils.toFraction;

public class CloseBidAskAnalyzer implements Analyzer {

    public static final Set<Integer> PERCENTAGES = Sets.newHashSet(1, 2, 3, 5, 10, 20);

    private final int percent;

    private BigDecimal totalBids = BigDecimal.ZERO;
    private BigDecimal totalAsks = BigDecimal.ZERO;
    private BigDecimal price;

    public CloseBidAskAnalyzer(final int percent) {
        this.percent = percent;
    }

    @Override
    public void analyze(final OrderBook orderBook) {
        final BigDecimal bestAsk = orderBook.getAsks().getBestPrice();
        final BigDecimal bestBid = orderBook.getBids().getBestPrice();

        if (bestAsk == null || bestBid == null) {
            return;
        }

        this.price = avgPrice(bestAsk, bestBid);

        final BigDecimal fraction = toFraction(percent);
        final BigDecimal maxAsk= price.multiply(BigDecimal.ONE.add(fraction));
        final BigDecimal minBid = price.multiply(BigDecimal.ONE.subtract(fraction));

        orderBook.getAsks().forEach(order -> {
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

    public BigDecimal getPrice() {
        return price;
    }
}
