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

        final Instant oneHourAgo = Instant.now().minus(1, HOURS);

        System.out.println("Got " + coinData.size() + " symbols");

        assertThat(coinData.size(), greaterThan(100));

        Map<String, CoinDatum> map = coinData.stream().collect(Collectors.toMap(CoinDatum::getId, Function.identity()));

        {
            CoinDatum bitcoin = map.get("bitcoin");
            assertThat(bitcoin.getId(), equalTo("bitcoin"));
            assertThat(bitcoin.getName(), equalTo("Bitcoin"));
            assertThat(bitcoin.getSymbol(), equalTo("BTC"));
            assertThat(bitcoin.getAvailableSupply(), greaterThan(new BigDecimal("16000000")));
            assertThat(bitcoin.getMaxSupply(), equalTo(new BigDecimal("21000000")));
            assertThat(bitcoin.getLastUpdated(), greaterThan(oneHourAgo));
        }

        // test uncapped supply
        {
            CoinDatum ethereum = map.get("ethereum");
            assertThat(ethereum.getId(), equalTo("ethereum"));
            assertThat(ethereum.getName(), equalTo("Ethereum"));
            assertThat(ethereum.getSymbol(), equalTo("ETH"));
            assertThat(ethereum.getAvailableSupply(), greaterThan(new BigDecimal("96000000")));
            assertThat(ethereum.getMaxSupply(), is(nullValue()));
            assertThat(ethereum.getLastUpdated(), greaterThan(oneHourAgo));
        }

        // test id and name override
        {
            CoinDatum bcash = map.get("bcash");
            assertThat(bcash.getId(), equalTo("bcash"));
            assertThat(bcash.getName(), equalTo("Bcash"));
            assertThat(bcash.getSymbol(), equalTo("BCH"));
            assertThat(bcash.getAvailableSupply(), greaterThan(new BigDecimal("16000000")));
            assertThat(bcash.getMaxSupply(), equalTo(new BigDecimal("21000000")));
            assertThat(bcash.getLastUpdated(), greaterThan(oneHourAgo));
        }

        // test max supply override
        {
            CoinDatum zcash = map.get("zcash");
            assertThat(zcash.getId(), equalTo("zcash"));
            assertThat(zcash.getName(), equalTo("Zcash"));
            assertThat(zcash.getSymbol(), equalTo("ZEC"));
            assertThat(zcash.getAvailableSupply(), greaterThan(new BigDecimal("2800000")));
            assertThat(zcash.getMaxSupply(), equalTo(new BigDecimal("21000000")));
            assertThat(zcash.getLastUpdated(), greaterThan(oneHourAgo));
        }
    }
}
