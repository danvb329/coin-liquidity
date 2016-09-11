package com.coinliquidity.web;

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.math.BigDecimal;

public class LiquidityDatum implements Comparable<LiquidityDatum> {

    private String exchange;
    private String currencyPair;
    private BigDecimal buyCost;
    private BigDecimal sellCost;
    private BigDecimal bestAsk;
    private BigDecimal bestBid;
    private BigDecimal totalAsks;
    private BigDecimal totalBids;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(final String exchange) {
        this.exchange = exchange;
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(final String currencyPair) {
        this.currencyPair = currencyPair;
    }

    public BigDecimal getBuyCost() {
        return buyCost;
    }

    public void setBuyCost(final BigDecimal buyCost) {
        this.buyCost = buyCost;
    }

    public BigDecimal getSellCost() {
        return sellCost;
    }

    public void setSellCost(final BigDecimal sellCost) {
        this.sellCost = sellCost;
    }

    public BigDecimal getBestAsk() {
        return bestAsk;
    }

    public void setBestAsk(final BigDecimal bestAsk) {
        this.bestAsk = bestAsk;
    }

    public BigDecimal getBestBid() {
        return bestBid;
    }

    public void setBestBid(final BigDecimal bestBid) {
        this.bestBid = bestBid;
    }

    public BigDecimal getTotalAsks() {
        return totalAsks;
    }

    public void setTotalAsks(BigDecimal totalAsks) {
        this.totalAsks = totalAsks;
    }

    public BigDecimal getTotalBids() {
        return totalBids;
    }

    public void setTotalBids(BigDecimal totalBids) {
        this.totalBids = totalBids;
    }

    @Override
    public int compareTo(final LiquidityDatum o) {
        return new CompareToBuilder()
                .append(sellCost, o.sellCost)
                .append(buyCost, o.buyCost)
                .build();
    }
}
