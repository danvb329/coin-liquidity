package com.coinliquidity.core.analyzer;

import com.coinliquidity.core.model.CurrencyPair;
import com.coinliquidity.core.model.OrderBook;
import com.coinliquidity.core.model.Orders;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class SlippageAnalyzerTest {

    private OrderBook orderBook;

    @Before
    public void setUp() {
        final Orders bids = new Orders();
        bids.put(new BigDecimal("80"), new BigDecimal("10"));
        bids.put(new BigDecimal("90"), new BigDecimal("10"));
        bids.put(new BigDecimal("100"), new BigDecimal("10"));
        final Orders asks = new Orders();
        asks.put(new BigDecimal("100"), new BigDecimal("10"));
        asks.put(new BigDecimal("110"), new BigDecimal("10"));
        asks.put(new BigDecimal("120"), new BigDecimal("10"));

        orderBook = new OrderBook("exchange", new CurrencyPair("BTC", "USD"), bids, asks);
    }

    // TODO: 9/10/16  test best bid & ask
    @Test
    public void test_firstOrder() {
        final SlippageAnalyzer slippageAnalyzer = new SlippageAnalyzer(new BigDecimal("1000"));
        slippageAnalyzer.analyze(orderBook);

        assertEquals(new BigDecimal("0.0000"), slippageAnalyzer.getBuyCost());
        assertEquals(new BigDecimal("0.0000"), slippageAnalyzer.getSellCost());
    }

    @Test
    public void test_firstTwoOrders() {
        /*
         * Bids:
         * 100 * 10 = 1000
         *  90 * 10 =  900
         * 1900 / 20 = 95
         *
         *  Asks:
         *  100 * 10   = 1000
         *  110 * 8.81 =  900
         *  1900 / 18.81 = 95.5
         */
        final SlippageAnalyzer slippageAnalyzer = new SlippageAnalyzer(new BigDecimal("1900"));
        slippageAnalyzer.analyze(orderBook);
        assertEquals(new BigDecimal("4.5000"), slippageAnalyzer.getBuyCost());
        assertEquals(new BigDecimal("5.0000"), slippageAnalyzer.getSellCost());
    }

    @Test
    public void test_notEnoughLiquidity() {
        final SlippageAnalyzer slippageAnalyzer = new SlippageAnalyzer(new BigDecimal("10000"));
        slippageAnalyzer.analyze(orderBook);
        assertEquals(SlippageAnalyzer.INVALID, slippageAnalyzer.getBuyCost());
        assertEquals(SlippageAnalyzer.INVALID, slippageAnalyzer.getSellCost());
    }
}
