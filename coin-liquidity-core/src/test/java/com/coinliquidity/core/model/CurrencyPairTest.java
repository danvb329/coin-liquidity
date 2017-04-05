package com.coinliquidity.core.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class CurrencyPairTest {

    private final String ccy;
    private final String expectedCcy;

    public CurrencyPairTest(final String ccy, final String expectedCcy) {
        this.ccy = ccy;
        this.expectedCcy = expectedCcy;
    }

    @Parameterized.Parameters(name = "{0}={1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "XBT", "BTC" },
                { "RUR", "RUB" },
                { "USDT", "USD" },
                { "DSH", "DASH" }
        });
    }

    @Test
    public void testNormalize() {
        final CurrencyPair currencyPair = new CurrencyPair(ccy, "USD").normalize();
        assertThat(currencyPair.getBaseCurrency(), equalTo(expectedCcy));
    }
}
