package com.coinliquidity.core.model;

import com.coinliquidity.core.FxConverter;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderBook {

    private final String exchange;
    private CurrencyPair currencyPair;
    private final CurrencyPair originalCurrencyPair;
    private final Orders bids;
    private final Orders asks;

    public OrderBook(final String exchange, final CurrencyPair currencyPair,
                     final Orders bids, final Orders asks) {
        this.exchange = exchange;
        this.currencyPair = currencyPair;
        this.originalCurrencyPair = currencyPair;
        this.bids = bids;
        this.asks = asks;
    }

    public Orders getBids() {
        return bids;
    }

    public Orders getAsks() {
        return asks;
    }

    public CurrencyPair getOriginalCurrencyPair() {
        return originalCurrencyPair;
    }

    public void convert(final FxConverter fxConverter) {
        if (!fxConverter.getBaseCcy().equals(currencyPair.getQuoteCurrency())) {
            final BigDecimal rate = fxConverter.getRate(currencyPair.getQuoteCurrency());
            bids.convert(rate);
            asks.convert(rate);
            this.currencyPair = new CurrencyPair(currencyPair.getBaseCurrency(), fxConverter.getBaseCcy());
        }
    }

    @Override
    public String toString() {
        return "OrderBook{" +
                "currencyPair=" + currencyPair +
                ", bids=" + bids +
                ", asks=" + asks +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderBook orderBook = (OrderBook) o;
        return Objects.equals(currencyPair, orderBook.currencyPair) &&
                Objects.equals(bids, orderBook.bids) &&
                Objects.equals(asks, orderBook.asks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bids, asks, currencyPair);
    }

    public CurrencyPair getCurrencyPair() {
        return currencyPair;
    }

    public String getName() {
        return exchange;
    }
}
