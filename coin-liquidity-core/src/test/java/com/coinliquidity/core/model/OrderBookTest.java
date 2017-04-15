package com.coinliquidity.core.model;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class OrderBookTest {

    @Test
    public void midPrice() {
        final OrderBook orderBook = new OrderBook("exchange", CurrencyPair.BTC_USD);
        orderBook.getBids().put(100, 100);
        orderBook.getAsks().put(101, 200);
        assertThat(orderBook.getMidPrice(), equalTo(new BigDecimal("100.50")));
    }
}
