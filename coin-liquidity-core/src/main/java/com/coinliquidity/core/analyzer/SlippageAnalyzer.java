package com.coinliquidity.core.analyzer;

import com.coinliquidity.core.model.Order;
import com.coinliquidity.core.model.OrderBook;

import java.math.BigDecimal;
import java.util.List;

public class SlippageAnalyzer implements Analyzer {

    private final BigDecimal amount;
    private BigDecimal buyCost;
    private BigDecimal sellCost;
    private BigDecimal bestAsk;
    private BigDecimal bestBid;

    public SlippageAnalyzer(final BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public void analyze(final OrderBook orderBook) {
        final List<Order> asks = orderBook.getAsks().ascending();
        final List<Order> bids = orderBook.getBids().descending();

        bestAsk = asks.isEmpty() ? null : asks.get(0).getPrice();
        bestBid = bids.isEmpty() ? null : bids.get(0).getPrice();

        buyCost = calculateCost(asks, bestAsk);
        sellCost = calculateCost(bids, bestBid);
    }

    private BigDecimal calculateCost(final List<Order> orders, final BigDecimal bestPrice) {
        if (orders.isEmpty()) {
            return null;
        }

        BigDecimal totalUnits = BigDecimal.ZERO;
        BigDecimal remaining = amount;
        for (final Order order : orders) {
            final BigDecimal orderAmount = order.getAmount();
            if (remaining.compareTo(orderAmount) > 0) {
                totalUnits = totalUnits.add(order.getUnits());
                remaining = remaining.subtract(orderAmount);
            } else {
                totalUnits = totalUnits.add(remaining.divide(order.getPrice(), 6, BigDecimal.ROUND_HALF_UP));
                remaining = BigDecimal.ZERO;
                break;
            }
        }

        if (remaining.compareTo(BigDecimal.ZERO) > 0) {
            return null;
        } else {
            final BigDecimal averagePrice = amount.divide(totalUnits, 6, BigDecimal.ROUND_HALF_UP);
            return percent(bestPrice, averagePrice);
        }
    }

    private BigDecimal percent(final BigDecimal number1, final BigDecimal number2) {
        BigDecimal result = number1.subtract(number2).multiply(new BigDecimal("100.00"));
        result = result.divide(number1, 4, BigDecimal.ROUND_HALF_UP).abs();
        return result;
    }

    public BigDecimal getBuyCost() {
        return buyCost;
    }

    public BigDecimal getSellCost() {
        return sellCost;
    }

    public BigDecimal getBestAsk() {
        return bestAsk;
    }

    public BigDecimal getBestBid() {
        return bestBid;
    }
}
