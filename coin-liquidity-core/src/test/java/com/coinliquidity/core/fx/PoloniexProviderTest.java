package com.coinliquidity.core.fx;

import com.coinliquidity.core.util.HttpClient;
import org.junit.Test;

import java.math.BigDecimal;

import static com.coinliquidity.core.model.CurrencyPair.BTC;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

public class PoloniexProviderTest {

    @Test
    public void getRates() {
        final FxRates rates = new PoloniexProvider(new HttpClient()).getRates();

        assertThat(rates.getRate(BTC), lessThan(new BigDecimal("0.01")));
        assertThat(rates.getInverseRate(BTC), greaterThan(new BigDecimal("100")));
    }

}