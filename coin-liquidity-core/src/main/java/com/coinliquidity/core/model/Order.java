package com.coinliquidity.core.model;

import com.coinliquidity.core.util.DecimalUtils;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Order {

    private BigDecimal price;
    private BigDecimal units;

    public Order(final BigDecimal price, final BigDecimal units) {
        this.price = price;
        this.units = units;
    }

    public Order(final int price, final int units) {
        this(new BigDecimal(price), new BigDecimal(units));
    }

    public BigDecimal getAmount() {
        return units.multiply(price);
    }

    public void convert(final BigDecimal rate) {
        this.price = DecimalUtils.convert(price, rate);
    }

    public void addUnits(final BigDecimal units) {
        this.units = this.units.add(units);
    }
}
