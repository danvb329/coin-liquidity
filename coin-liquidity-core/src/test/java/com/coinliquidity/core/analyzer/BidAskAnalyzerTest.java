package com.coinliquidity.core.analyzer;

import com.coinliquidity.core.model.CurrencyPair;
import com.coinliquidity.core.model.OrderBook;
import com.coinliquidity.core.model.Orders;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class BidAskAnalyzerTest {

    private OrderBook orderBook;

    @Before
    public void setUp() {
        orderBook = new OrderBook("exchange", CurrencyPair.BTC_USD);

        final Orders bids = orderBook.getBids();
        final Orders asks = orderBook.getAsks();

        bids.put(new BigDecimal(99), new BigDecimal(5));
        bids.put(new BigDecimal(90), new BigDecimal(7));
        bids.put(new BigDecimal(89), new BigDecimal(9));

        asks.put(new BigDecimal(101), new BigDecimal(2));
        asks.put(new BigDecimal(110), new BigDecimal(3));
        asks.put(new BigDecimal(111), new BigDecimal(5));
    }

    @Test
    public void test_10() {
        final BidAskAnalyzer analyzer = new BidAskAnalyzer(10);

        analyzer.analyze(orderBook);

        assertThat(analyzer.getTotalBids(), equalTo(new BigDecimal(12)));
        assertThat(analyzer.getTotalAsks(), equalTo(new BigDecimal(5)));

        // (99 * 5) + (90 * 7)
        // 495 + 630 = 1125
        assertThat(analyzer.getTotalBidsUsd(), equalTo(new BigDecimal("1125.00")));
        // (101 * 2) + (110 + 3)
        // 202 + 330 = 532
        assertThat(analyzer.getTotalAsksUsd(), equalTo(new BigDecimal("532.00")));
    }

    @Test
    public void test_all() {
        final BidAskAnalyzer analyzer = new BidAskAnalyzer(0);

        analyzer.analyze(orderBook);

        assertThat(analyzer.getTotalBids(), equalTo(new BigDecimal(21)));
        assertThat(analyzer.getTotalAsks(), equalTo(new BigDecimal(10)));

        // (99 * 5) + (90 * 7) + (89 * 9)
        // 495 + 630 + 801 = 1125
        assertThat(analyzer.getTotalBidsUsd(), equalTo(new BigDecimal("1926.00")));
        // (101 * 2) + (110 + 3) + (111 * 5)
        // 202 + 330 + 555 = 1087
        assertThat(analyzer.getTotalAsksUsd(), equalTo(new BigDecimal("1087.00")));
    }

}
