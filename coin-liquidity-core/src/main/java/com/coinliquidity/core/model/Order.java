package com.coinliquidity.core.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Order {

    private BigDecimal price;
    private BigDecimal units;

    public Order(final BigDecimal price, final BigDecimal units) {
        this.price = price;
        this.units = units;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getUnits() {
        return units;
    }

    public BigDecimal getAmount() {
        return units.multiply(price);
    }

    public void convert(final BigDecimal rate) {
        this.price = price.divide(rate, 5, BigDecimal.ROUND_HALF_UP);
    }

    public void addUnits(final BigDecimal units) {
        this.units = this.units.add(units);
    }

    @Override
    public String toString() {
        return units + " @ " + price;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Order order = (Order) o;
        return Objects.equals(price, order.price) &&
                Objects.equals(units, order.units);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, units);
    }
}
