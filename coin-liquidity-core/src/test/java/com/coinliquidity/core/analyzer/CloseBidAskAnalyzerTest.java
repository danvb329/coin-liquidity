package com.coinliquidity.core.analyzer;

import com.coinliquidity.core.model.CurrencyPair;
import com.coinliquidity.core.model.OrderBook;
import com.coinliquidity.core.model.Orders;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class CloseBidAskAnalyzerTest {

    @Test
    public void test() {
        final CloseBidAskAnalyzer analyzer = new CloseBidAskAnalyzer(10);

        final OrderBook orderBook = new OrderBook("exchange", CurrencyPair.BTC_USD);

        final Orders asks = orderBook.getAsks();
        final Orders bids = orderBook.getBids();

        asks.put(new BigDecimal(101), new BigDecimal(2));
        asks.put(new BigDecimal(110), new BigDecimal(3));
        asks.put(new BigDecimal(111), new BigDecimal(5));

        bids.put(new BigDecimal(99), new BigDecimal(5));
        bids.put(new BigDecimal(90), new BigDecimal(7));
        bids.put(new BigDecimal(89), new BigDecimal(12));

        analyzer.analyze(orderBook);

        assertThat(analyzer.getPrice(), equalTo(new BigDecimal("100.00")));
        assertThat(analyzer.getTotalAsks(), equalTo(new BigDecimal(5)));

        // (99 * 5) + (90 * 7)
        // 495 + 630 = 1125
        assertThat(analyzer.getTotalBids(), equalTo(new BigDecimal("1125.00")));
    }

}
