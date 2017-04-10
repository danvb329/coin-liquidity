package com.coinliquidity.core.fx;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

public class FixerIoProviderTest {

    @Test
    public void test() {
        final FixerIoProvider fixerIoProvider = new FixerIoProvider("USD");

        final FxRates rates = fixerIoProvider.getRates();

        assertThat(fixerIoProvider.getDataDate(), greaterThan(LocalDate.now().minusDays(4)));
        assertThat(rates.getRate("USD"), equalTo(BigDecimal.ONE));
        assertThat(rates.getRate("EUR"), greaterThan(BigDecimal.ZERO));
    }
}
