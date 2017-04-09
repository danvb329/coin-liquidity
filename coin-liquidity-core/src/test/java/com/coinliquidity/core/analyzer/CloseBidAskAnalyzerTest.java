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
        final CloseBidAskAnalyzer analyzer = new CloseBidAskAnalyzer(
                new BigDecimal(100),10);

        final Orders bids = new Orders();
        final Orders asks = new Orders();

        final OrderBook orderBook = new OrderBook("",
                new CurrencyPair("BTC", "USD"), bids, asks);

        asks.put(new BigDecimal(101), new BigDecimal(2));
        asks.put(new BigDecimal(110), new BigDecimal(3));
        asks.put(new BigDecimal(111), new BigDecimal(5));

        bids.put(new BigDecimal(98), new BigDecimal(5));
        bids.put(new BigDecimal(90), new BigDecimal(7));
        bids.put(new BigDecimal(89), new BigDecimal(12));

        analyzer.analyze(orderBook);

        assertThat(analyzer.getTotalAsks(), equalTo(new BigDecimal(5)));

        // (98 * 5) + (90 * 7)
        // 490 + 630 = 1120
        assertThat(analyzer.getTotalBids(), equalTo(new BigDecimal("1120.00")));

    }

}
