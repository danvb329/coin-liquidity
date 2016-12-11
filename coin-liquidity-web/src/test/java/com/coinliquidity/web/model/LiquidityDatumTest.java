package com.coinliquidity.web.model;

import com.coinliquidity.core.model.CurrencyPair;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
}
