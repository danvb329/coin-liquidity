package com.coinliquidity.core.coindata;

import com.coinliquidity.core.model.CoinDatum;
import com.coinliquidity.core.util.HttpClient;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.HOURS;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

public class CoinDataDownloaderTest {

    private CoinDataDownloader coinDataDownloader;

    @Before
    public void setUp() {
        coinDataDownloader = new CoinDataDownloader("https://api.coinmarketcap.com/v1/ticker?limit=0",
                new HttpClient());
    }

    @Test
    public void downloadData() {
        final Instant now = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC);
        List<CoinDatum> coinData = coinDataDownloader.downloadData(now);

        System.out.println("Got " + coinData.size() + " symbols");

        assertThat(coinData.size(), greaterThan(100));
        CoinDatum first = coinData.get(0);
        assertThat(first.getSymbol(), equalTo("BTC"));
        assertThat(first.getAvailableSupply(), greaterThan(new BigDecimal("16000000.0")));
        assertThat(first.getMaxSupply(), equalTo(new BigDecimal("21000000.0")));
        assertThat(first.getLastUpdated(), greaterThan(Instant.now().minus(1, HOURS)));

        // check for duplicates
        coinData.stream().collect(Collectors.toMap(CoinDatum::getSymbol, Function.identity()));
    }
}
