package com.coinliquidity.core.fx;

import com.coinliquidity.core.model.CurrencyPair;
import com.coinliquidity.core.util.HttpClient;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

public class FixerIoProviderTest {

    @Test
    public void test() {
        final FixerIoProvider fixerIoProvider = new FixerIoProvider(new HttpClient(), CurrencyPair.USD);

        final FxRates rates = fixerIoProvider.getRates();

        assertThat(fixerIoProvider.getDataDate(), greaterThan(LocalDate.now().minusDays(5)));
        assertThat(rates.getRate("USD"), equalTo(BigDecimal.ONE));
        assertThat(rates.getRate("CNY"), greaterThan(BigDecimal.ONE));
    }
}
