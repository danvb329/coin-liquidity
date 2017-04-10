package com.coinliquidity.core.util;

import org.junit.Test;

import java.math.BigDecimal;

import static com.coinliquidity.core.util.DecimalUtils.scalePrice;
import static org.junit.Assert.assertEquals;

public class DecimalUtilsTest {

    @Test
    public void scalePrice_large() {
        assertEquals(new BigDecimal("1.23"), scalePrice(new BigDecimal("1.234567")));
    }

    @Test
    public void scalePrice_small() {
        assertEquals(new BigDecimal("0.12346"), scalePrice(new BigDecimal("0.1234567")));
    }

}