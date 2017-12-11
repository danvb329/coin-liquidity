package com.coinliquidity.web;

import com.coinliquidity.core.model.CoinDatum;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class CoinDataCacheTest {

    private CoinDataCache coinDataCache;

    @Before
    public void setUp() {
        coinDataCache = new CoinDataCache(null, null);
    }

    @Test
    public void calculateInflation() {
        CoinDatum current = new CoinDatum();
        current.setAvailableSupply(BigDecimal.valueOf(1200));

        CoinDatum prior = new CoinDatum();
        prior.setAvailableSupply(BigDecimal.valueOf(1000));

        BigDecimal inflation = coinDataCache.calculateInflation(current, prior, 365);
        assertThat(inflation, equalTo(new BigDecimal("20.0")));
    }

    @Test
    public void calculateInflation_BTC2017() {
        CoinDatum current = new CoinDatum();
        current.setAvailableSupply(BigDecimal.valueOf(16_715_938));

        CoinDatum prior = new CoinDatum();
        prior.setAvailableSupply(BigDecimal.valueOf(16_022_513));

        BigDecimal inflation = coinDataCache.calculateInflation(current, prior, 365);
        assertThat(inflation, equalTo(new BigDecimal("4.3")));
    }

    @Test
    public void calculateInflationUsd() {
        CoinDatum current = new CoinDatum();
        current.setAvailableSupply(BigDecimal.valueOf(1100));
        current.setPriceUsd(BigDecimal.valueOf(50));

        CoinDatum prior = new CoinDatum();
        prior.setAvailableSupply(BigDecimal.valueOf(1000));

        BigDecimal inflationUsd = coinDataCache.calculateInflationUsd(current, prior, 5);
        assertThat(inflationUsd, equalTo(new BigDecimal("1000")));
    }
}