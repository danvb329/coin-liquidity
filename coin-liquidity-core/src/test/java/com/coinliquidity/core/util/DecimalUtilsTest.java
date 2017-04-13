package com.coinliquidity.core.util;

import org.junit.Test;

import java.math.BigDecimal;

import static com.coinliquidity.core.util.DecimalUtils.scalePrice;
import static org.junit.Assert.assertEquals;

public class DecimalUtilsTest {

    @Test
    public void scalePrice_belowMax() {
        assertEquals(new BigDecimal("0.1234567"), scalePrice(new BigDecimal("0.1234567")));
    }

    @Test
    public void scalePrice_aboveMax() {
        assertEquals(new BigDecimal("0.12345679"), scalePrice(new BigDecimal("0.123456789")));
    }

    @Test
    public void scalePrice_trim() {
        assertEquals(new BigDecimal("0.12345"), scalePrice(new BigDecimal("0.12345000")));
    }

    @Test
    public void scalePrice_trimMin() {
        assertEquals(new BigDecimal("500.00"), scalePrice(new BigDecimal("500")));
    }

    @Test
    public void avgPrice() {
        final BigDecimal p1 = new BigDecimal("0.25");
        final BigDecimal p2 = new BigDecimal("0.00");
        assertEquals(new BigDecimal("0.125"), DecimalUtils.avgPrice(p1, p2));
    }

    @Test
    public void avgPrice_rounding() {
        final BigDecimal p1 = new BigDecimal("0.00000001");
        final BigDecimal p2 = new BigDecimal("0.00000002");
        assertEquals(new BigDecimal("0.00000002"), DecimalUtils.avgPrice(p1, p2));
    }

    @Test
    public void inverseRate() {
        assertEquals(new BigDecimal("0.0001"), DecimalUtils.inverseRate(new BigDecimal("10000")));
    }

    @Test
    public void toFraction() {
        assertEquals(new BigDecimal("0.42"), DecimalUtils.toFraction(42));
    }

    @Test
    public void convert() {
        final BigDecimal price = new BigDecimal("100.000");
        final BigDecimal rate = new BigDecimal("32");
        assertEquals(new BigDecimal("3.13"), DecimalUtils.convert(price, rate));
    }

}