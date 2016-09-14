package com.coinliquidity.core.model;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class OrdersTest {

    @Test
    public void testMerge() {
        final Orders orders1 = new Orders();
        orders1.put(new BigDecimal("100"), new BigDecimal("5"));

        final Orders orders2 = new Orders();
        orders2.put(new BigDecimal("100"), new BigDecimal("6"));

        final BigDecimal rate = new BigDecimal("3");
        final Orders merged = orders1.merge(orders2, rate);

        final Orders expected = new Orders();
        expected.put(new BigDecimal("100"), new BigDecimal("5"));
        expected.put(new BigDecimal("33.33"), new BigDecimal("6"));

        assertEquals(expected, merged);
    }
}
