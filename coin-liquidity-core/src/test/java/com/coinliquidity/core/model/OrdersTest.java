package com.coinliquidity.core.model;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OrdersTest {

    @Test
    public void testMerge() {
        final Orders orders1 = Orders.bids();
        orders1.put(100, 5);

        final Orders orders2 = Orders.bids();
        orders2.put(100, 6);

        final Orders merged = orders1.merge(orders2);

        final Orders expected = Orders.bids();
        expected.put(new BigDecimal("100"), new BigDecimal("5"));
        expected.put(new BigDecimal("100"), new BigDecimal("6"));

        assertEquals(expected, merged);
    }

    @Test
    public void testAdd() {
        final Orders orders = Orders.bids();
        orders.put(100, 5);
        orders.put(100, 3);
        orders.put(101, 7);

        final Orders expected = Orders.bids();
        expected.put(100, 8);
        expected.put(101, 7);

        assertEquals(expected, orders);
    }

    @Test
    public void testGetBestPrice_bids() {
        final Orders orders = Orders.bids();
        orders.put(101, 5);
        orders.put(100, 3);
        orders.put(102, 7);

        // highest bid
        assertEquals(new BigDecimal(102), orders.getBestPrice());
    }

    @Test
    public void testGetBestPrice_asks() {
        final Orders orders = Orders.asks();
        orders.put(101, 5);
        orders.put(100, 3);
        orders.put(102, 7);

        // lowest ask
        assertEquals(new BigDecimal(100), orders.getBestPrice());
    }

    @Test
    public void testGetBestPrice_empty() {
        final Orders orders = Orders.asks();

        // lowest ask
        assertNull(orders.getBestPrice());
    }

    @Test
    public void testSortBids() {
        final Orders orders = Orders.bids();
        orders.put(101, 1);
        orders.put(103, 1);
        orders.put(100, 1);
        orders.put(102, 1);

        final Iterator<Order> it = orders.iterator();
        assertEquals(new Order(103, 1), it.next());
        assertEquals(new Order(102, 1), it.next());
        assertEquals(new Order(101, 1), it.next());
        assertEquals(new Order(100, 1), it.next());
    }

    @Test
    public void testSortAsks() {
        final Orders orders = Orders.asks();
        orders.put(101, 1);
        orders.put(103, 1);
        orders.put(100, 1);
        orders.put(102, 1);

        final Iterator<Order> it = orders.iterator();
        assertEquals(new Order(100, 1), it.next());
        assertEquals(new Order(101, 1), it.next());
        assertEquals(new Order(102, 1), it.next());
        assertEquals(new Order(103, 1), it.next());
    }

    @Test
    public void testConvert() {
        final Orders orders = Orders.bids();
        orders.put(100, 5);
        orders.put(200, 3);
        orders.put(300, 7);

        orders.convert(new BigDecimal(10));

        final Orders expected = Orders.bids();
        expected.put(new BigDecimal("10.00"), new BigDecimal(5));
        expected.put(new BigDecimal("20.00"), new BigDecimal(3));
        expected.put(new BigDecimal("30.00"), new BigDecimal(7));

        assertEquals(expected, orders);
    }
}
