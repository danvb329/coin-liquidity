package com.coinliquidity.core.fx;

import com.coinliquidity.core.util.HttpClient;
import org.junit.Test;

import java.math.BigDecimal;

import static com.coinliquidity.core.fx.KrakenProvider.USDT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class KrakenProviderTest {

    @Test
    public void getRates() {
        final KrakenProvider krakenProvider = new KrakenProvider(new HttpClient());
        krakenProvider.refresh();
        final FxRates rates = krakenProvider.getRates();
        assertThat(rates.getRate(USDT), greaterThan(BigDecimal.ONE));
    }

}