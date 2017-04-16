package com.coinliquidity.core.analyzer;

import com.coinliquidity.core.model.OrderBook;
import com.coinliquidity.core.util.DecimalUtils;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.coinliquidity.core.util.DecimalUtils.toFraction;

public class BidAskAnalyzer implements Analyzer {

    public static final List<Integer> PERCENTAGES = Lists.newArrayList(1, 2, 3, 5, 10, 20, 0);

    private final int percent;

    private BigDecimal totalBids = BigDecimal.ZERO;
    private BigDecimal totalBidsUsd = BigDecimal.ZERO;
    private BigDecimal totalAsks = BigDecimal.ZERO;
    private BigDecimal totalAsksUsd = BigDecimal.ZERO;

    public BidAskAnalyzer(final int percent) {
        this.percent = percent;
    }

    @Override
    public void analyze(final OrderBook orderBook) {
        final BigDecimal price = orderBook.getMidPrice();

        if (price == null) {
            return;
        }

        final BigDecimal maxAsk;
        final BigDecimal minBid;

        if (percent != 0) {
            final BigDecimal fraction = toFraction(percent);
            maxAsk = price.multiply(BigDecimal.ONE.add(fraction));
            minBid = price.multiply(BigDecimal.ONE.subtract(fraction));
        } else {
            // add up all bids and asks
            maxAsk = DecimalUtils.MAX;
            minBid = BigDecimal.ZERO;
        }

        orderBook.getAsks().forEach(order -> {
            if (order.getPrice().compareTo(maxAsk) <= 0) {
                totalAsks = totalAsks.add(order.getUnits());
                totalAsksUsd = totalAsksUsd.add(order.getAmount());
            }
        });
        orderBook.getBids().forEach(order -> {
            if (order.getPrice().compareTo(minBid) >= 0) {
                totalBids = totalBids.add(order.getUnits());
                totalBidsUsd = totalBidsUsd.add(order.getAmount());
            }
        });
    }

    public BigDecimal getTotalBids() {
        return totalBids.setScale(0, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalAsks() {
        return totalAsks.setScale(0, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalBidsUsd() {
        return totalBidsUsd.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalAsksUsd() {
        return totalAsksUsd.setScale(2, RoundingMode.HALF_UP);
    }
}
