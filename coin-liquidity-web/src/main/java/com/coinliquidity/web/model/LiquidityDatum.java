package com.coinliquidity.web.model;

import com.coinliquidity.core.model.CurrencyPair;

import java.math.BigDecimal;

import static com.coinliquidity.core.model.CurrencyPair.BTC;

public class LiquidityDatum {

    private String exchange;
    private CurrencyPair currencyPair;
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

    public CurrencyPair getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(final CurrencyPair currencyPair) {
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

    public boolean matches(final String baseCurrency, final String quoteCurrency) {
        return isMatch(baseCurrency, currencyPair.getBaseCurrency())
                && isMatch(quoteCurrency, currencyPair.getQuoteCurrency());
    }

    public boolean matches(final String exchange) {
        return "*".equals(exchange) || this.exchange.equals(exchange);
    }

    private static boolean isMatch(final String testCurrency, final String currency) {
        return "*".equals(testCurrency) || currency.equals(testCurrency) ||
                ("ALT".equals(testCurrency) && !BTC.equals(currency));
    }

    String getBaseCurrency() {
        return currencyPair.getBaseCurrency();
    }

    String getQuoteCurrency() {
        return currencyPair.getQuoteCurrency();
    }
}
