package com.coinliquidity.core.model;

import com.coinliquidity.core.fx.FxRates;
import lombok.Data;

import java.math.BigDecimal;

import static com.coinliquidity.core.util.DecimalUtils.avgPrice;
import static com.google.common.base.Preconditions.checkArgument;

@Data
public class OrderBook {

    private final String exchange;
    private CurrencyPair currencyPair;
    private final CurrencyPair originalCurrencyPair;
    private final Orders bids;
    private final Orders asks;

    public OrderBook(final String exchange, final CurrencyPair currencyPair) {
        this.exchange = exchange;
        this.currencyPair = currencyPair;
        this.originalCurrencyPair = currencyPair;
        this.bids = Orders.bids();
        this.asks = Orders.asks();
    }

    public void convert(final FxRates fxRates) {
        if (!fxRates.getBaseCcy().equals(currencyPair.getQuoteCurrency())) {
            final BigDecimal rate = fxRates.getRate(currencyPair.getQuoteCurrency());
            bids.convert(rate);
            asks.convert(rate);
            this.currencyPair = new CurrencyPair(currencyPair.getBaseCurrency(), fxRates.getBaseCcy());
        }
    }

    public String getName() {
        return exchange;
    }

    public BigDecimal getMidPrice() {
        final BigDecimal bestBid = bids.getBestPrice();
        final BigDecimal bestAsk = asks.getBestPrice();

        if (bestBid != null && bestAsk != null) {
            return avgPrice(bestBid, bestAsk);
        } else {
            return null;
        }
    }

    public void merge(final OrderBook other) {
        checkArgument(other.currencyPair.equals(this.currencyPair), "must have same currency pair");
        this.bids.merge(other.bids);
        this.asks.merge(other.asks);
    }
}
