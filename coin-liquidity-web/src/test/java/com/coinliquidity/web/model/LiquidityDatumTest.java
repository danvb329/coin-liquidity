package com.coinliquidity.web.model;

import com.coinliquidity.core.model.CurrencyPair;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class LiquidityDatumTest {

    @Test
    public void testMatchesCurrency() {
        final LiquidityDatum datum = new LiquidityDatum();
        datum.setCurrencyPair(new CurrencyPair("BTC", "USD"));

        assertTrue(datum.matches("BTC", "USD"));
        assertTrue(datum.matches("*", "USD"));
        assertTrue(datum.matches("BTC", "*"));
        assertTrue(datum.matches("*", "*"));

        assertFalse(datum.matches("ETH", "EUR"));
        assertFalse(datum.matches("ETH", "*"));
        assertFalse(datum.matches("*", "EUR"));
        assertFalse(datum.matches("ALT", "USD"));

        datum.setCurrencyPair(new CurrencyPair("ETH", "USD"));

        assertTrue(datum.matches("ALT", "USD"));
    }

    @Test
    public void testScore() {
        final LiquidityDatum datum = new LiquidityDatum();
        datum.setBidsUsd(0, new BigDecimal(10000));
        datum.setBidsUsd(1, new BigDecimal(100));
        datum.setBidsUsd(2, new BigDecimal(120));
        datum.setBidsUsd(3, new BigDecimal(150));
        datum.setBidsUsd(5, new BigDecimal(200));
        datum.setBidsUsd(10, new BigDecimal(300));
        datum.setBidsUsd(20, new BigDecimal(500));

        datum.setAsksUsd(0, new BigDecimal(10000));
        datum.setAsksUsd(1, new BigDecimal(100));
        datum.setAsksUsd(2, new BigDecimal(120));
        datum.setAsksUsd(3, new BigDecimal(150));
        datum.setAsksUsd(5, new BigDecimal(200));
        datum.setAsksUsd(10, new BigDecimal(300));
        datum.setAsksUsd(20, new BigDecimal(500));

        assertThat(datum.getLiquidityScore(), equalTo(new BigDecimal(150)));
    }
}
