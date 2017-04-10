package com.coinliquidity.core.fx;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static com.coinliquidity.core.model.CurrencyPair.BTC;
import static com.coinliquidity.core.model.CurrencyPair.USD;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

public class BitcoinAverageProviderTest {

    @Test
    public void test() {
        final BitcoinAverageProvider bitcoinAverageProvider = new BitcoinAverageProvider("USD");

        final FxRates rates = bitcoinAverageProvider.getRates();

        assertThat(rates.getRate(BTC), lessThan(new BigDecimal("0.01")));
        assertThat(rates.getInverseRate(BTC), greaterThan(new BigDecimal("100")));

        assertThat(rates.getUpdateTime(), lessThan(Instant.now()));
        assertThat(rates.getRate(USD), equalTo(BigDecimal.ONE));
    }
}
