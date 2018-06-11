package com.coinliquidity.core.fx;

import com.coinliquidity.core.model.CurrencyPair;
import com.coinliquidity.core.util.HttpClient;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

public class FixerIoProviderTest {

    @Test
    public void test() {
        String accessKey = System.getenv("FIXER_IO_ACCESS_KEY");
        if (accessKey == null) {
            accessKey = System.getProperty("FIXER_IO_ACCESS_KEY");
        }
        final FixerIoProvider fixerIoProvider = new FixerIoProvider(new HttpClient(), CurrencyPair.USD, accessKey);
        final FxRates rates = fixerIoProvider.getRates();
        fixerIoProvider.refresh();
        System.out.println(rates);
        assertThat(fixerIoProvider.getDataDate(), greaterThan(LocalDate.now().minusDays(5)));
        assertThat(rates.getRate("USD"), equalTo(BigDecimal.ONE));

        assertThat(rates.getRate("CNY"), greaterThan(new BigDecimal(6)));
        assertThat(rates.getRate("CNY"), lessThan(new BigDecimal(7)));

        assertThat(rates.getRate("EUR"), greaterThan(new BigDecimal("0.5")));
        assertThat(rates.getRate("EUR"), lessThan(new BigDecimal(1)));
    }
}
