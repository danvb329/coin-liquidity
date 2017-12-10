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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.HOURS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsNull.nullValue;
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

        Map<String, CoinDatum> map = coinData.stream().collect(Collectors.toMap(CoinDatum::getId, Function.identity()));

        {
            CoinDatum bitcoin = map.get("bitcoin");
            assertThat(bitcoin.getId(), equalTo("bitcoin"));
            assertThat(bitcoin.getName(), equalTo("Bitcoin"));
            assertThat(bitcoin.getSymbol(), equalTo("BTC"));
            assertThat(bitcoin.getAvailableSupply(), greaterThan(new BigDecimal("16000000.0")));
            assertThat(bitcoin.getMaxSupply(), equalTo(new BigDecimal("21000000.0")));
            assertThat(bitcoin.getLastUpdated(), greaterThan(Instant.now().minus(1, HOURS)));
        }

        {
            CoinDatum bitcoin = map.get("ethereum");
            assertThat(bitcoin.getId(), equalTo("ethereum"));
            assertThat(bitcoin.getName(), equalTo("Ethereum"));
            assertThat(bitcoin.getSymbol(), equalTo("ETH"));
            assertThat(bitcoin.getMaxSupply(), is(nullValue()));
            assertThat(bitcoin.getLastUpdated(), greaterThan(Instant.now().minus(1, HOURS)));
        }

        {
            CoinDatum bcash = map.get("bcash");
            assertThat(bcash.getId(), equalTo("bcash"));
            assertThat(bcash.getName(), equalTo("Bcash"));
            assertThat(bcash.getSymbol(), equalTo("BCH"));
            assertThat(bcash.getAvailableSupply(), greaterThan(new BigDecimal("16000000.0")));
            assertThat(bcash.getMaxSupply(), equalTo(new BigDecimal("21000000.0")));
            assertThat(bcash.getLastUpdated(), greaterThan(Instant.now().minus(1, HOURS)));
        }
    }
}
