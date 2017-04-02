package com.coinliquidity.core;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;

public class FxConverterTest {

    @Test
    public void test() {
        final FxConverter converter = new FxConverter("USD");
        final BigDecimal btcRate = converter.getRate("BTC");
        final BigDecimal eurRate = converter.getRate("EUR");

        System.out.println("BTC rate: " + btcRate);
        System.out.println("EUR rate: " + eurRate);

        assertTrue(btcRate.compareTo(BigDecimal.ZERO) > 0);
        assertTrue(eurRate.compareTo(BigDecimal.ZERO) > 0);
    }
}
