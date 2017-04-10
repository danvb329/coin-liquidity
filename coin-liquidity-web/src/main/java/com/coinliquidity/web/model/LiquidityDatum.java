package com.coinliquidity.web.model;

import com.coinliquidity.core.model.CurrencyPair;
import com.google.common.collect.Maps;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

import static com.coinliquidity.core.model.CurrencyPair.BTC;

@Data
public class LiquidityDatum {

    private String exchange;
    private CurrencyPair currencyPair;
    private BigDecimal buyCost;
    private BigDecimal sellCost;
    private BigDecimal bestAsk;
    private BigDecimal bestBid;
    private BigDecimal totalAsks;
    private BigDecimal totalBids;
    private Instant updateTime;
    private BigDecimal price;

    private final Map<Integer, BigDecimal> bids = Maps.newHashMap();
    private final Map<Integer, BigDecimal> asks = Maps.newHashMap();

    public BigDecimal getBids(final int percent) {
        return bids.get(percent);
    }

    public BigDecimal getAsks(final int percent) {
        return asks.get(percent);
    }

    public void setBids(final int percent, final BigDecimal value) {
        this.bids.put(percent, value);
    }

    public void setAsks(final int percent, final BigDecimal value) {
        this.asks.put(percent, value);
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

    public String getBaseCurrency() {
        return currencyPair.getBaseCurrency();
    }

    String getQuoteCurrency() {
        return currencyPair.getQuoteCurrency();
    }
}
