package com.coinliquidity.core.analyzer;

import com.coinliquidity.core.model.Order;
import com.coinliquidity.core.model.OrderBook;
import com.google.common.collect.Sets;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
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
        final List<Order> asks = orderBook.getAsks().ascending();
        final List<Order> bids = orderBook.getBids().descending();

        final BigDecimal bestAsk = asks.isEmpty() ? null : asks.get(0).getPrice();
        final BigDecimal bestBid = bids.isEmpty() ? null : bids.get(0).getPrice();

        if (bestAsk == null || bestBid == null) {
            return;
        }

        this.price = avgPrice(bestAsk, bestBid);

        final BigDecimal fraction = toFraction(percent);
        final BigDecimal maxAsk= price.multiply(BigDecimal.ONE.add(fraction));
        final BigDecimal minBid = price.multiply(BigDecimal.ONE.subtract(fraction));

        asks.forEach(order -> {
            if (order.getPrice().compareTo(maxAsk) <= 0) {
                totalAsks = totalAsks.add(order.getUnits());
            }
        });
        bids.forEach(order -> {
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
