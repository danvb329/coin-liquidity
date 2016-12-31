package com.coinliquidity.core.model;

import java.math.BigDecimal;
import java.util.*;

public class Orders implements Iterable<Order> {

    private Map<BigDecimal, Order> orderMap = new TreeMap<>();

    public void put(final BigDecimal price, final BigDecimal units) {
        final Order existingOrder = orderMap.get(price);

        if (existingOrder == null) {
            orderMap.put(price, new Order(price, units));
        } else {
            orderMap.put(price, new Order(price, units.add(existingOrder.getUnits())));
        }
    }

    Orders merge(final Orders other, final BigDecimal rate) {
        final Orders retVal = new Orders();
        for (final Order order : orderMap.values()) {
            retVal.put(order.getPrice(), order.getUnits());
        }
        for (final Order order : other) {
            final BigDecimal price = order.getPrice();
            final BigDecimal units = order.getUnits();
            retVal.put(price.divide(rate, 2, BigDecimal.ROUND_HALF_UP), units);
        }
        return retVal;
    }

    public List<Order> ascending() {
        final List<Order> retVal = new ArrayList<>();
        retVal.addAll(orderMap.values());
        retVal.sort((o1, o2) -> o1.getPrice().compareTo(o2.getPrice()));
        return retVal;
    }

    public List<Order> descending() {
        final List<Order> retVal = new ArrayList<>();
        retVal.addAll(orderMap.values());
        retVal.sort((o1, o2) -> o2.getPrice().compareTo(o1.getPrice()));
        return retVal;
    }

    public void convert(final BigDecimal rate) {
        orderMap.values().forEach(order -> order.convert(rate));
    }

    public int size() {
        return orderMap.size();
    }

    @Override
    public Iterator<Order> iterator() {
        return orderMap.values().iterator();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Orders orders = (Orders) o;
        return Objects.equals(orderMap, orders.orderMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderMap);
    }

    @Override
    public String toString() {
        return "Orders{" +
                "orderMap=" + orderMap +
                '}';
    }
}
