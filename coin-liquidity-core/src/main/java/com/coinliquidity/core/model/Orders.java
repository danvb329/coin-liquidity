package com.coinliquidity.core.model;

import com.google.common.collect.Maps;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

import static com.google.common.base.Preconditions.checkArgument;

@Data
public class Orders implements Iterable<Order> {

    private static final Comparator<BigDecimal> ASCENDING = Comparator.naturalOrder();
    private static final Comparator<BigDecimal> DESCENDING = Comparator.reverseOrder();

    private final TreeMap<BigDecimal, Order> orderMap;
    private final OrderType orderType;

    private Orders(final Comparator<BigDecimal> comparator, final OrderType orderType) {
        this.orderMap = new TreeMap<>(comparator);
        this.orderType = orderType;
    }

    public static Orders bids() {
        return new Orders(DESCENDING, OrderType.BID);
    }

    public static Orders asks() {
        return new Orders(ASCENDING, OrderType.ASK);
    }

    // for testing
    public void put(final int price, final int units) {
        this.put(BigDecimal.valueOf(price), BigDecimal.valueOf(units));
    }

    public void put(final BigDecimal price, final BigDecimal units) {
        final Order existingOrder = orderMap.get(price);

        if (existingOrder == null) {
            orderMap.put(price, new Order(price, units));
        } else {
            existingOrder.addUnits(units);
        }
    }

    public BigDecimal getBestPrice() {
        return orderMap.isEmpty() ? null : orderMap.firstKey();
    }

    public Orders merge(final Orders other) {
        checkArgument(this.orderType.equals(other.orderType), "Order types must equal");
        for (final Order order : other) {
            final BigDecimal price = order.getPrice();
            final BigDecimal units = order.getUnits();
            if (BigDecimal.ZERO.compareTo(units) == 0) {
                orderMap.remove(price);
            } else {
                this.put(price, units);
            }
        }
        return this;
    }

    public void convert(final BigDecimal rate) {
        final TreeMap<BigDecimal, Order> newMap = Maps.newTreeMap();
        orderMap.values().forEach(order -> {
            order.convert(rate);
            newMap.put(order.getPrice(), order);
        });

        orderMap.clear();
        orderMap.putAll(newMap);
    }

    public int size() {
        return orderMap.size();
    }

    @Override
    public Iterator<Order> iterator() {
        return orderMap.values().iterator();
    }
}
