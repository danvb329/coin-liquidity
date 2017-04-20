package com.coinliquidity.core.download;

import com.coinliquidity.core.model.CurrencyPair;
import com.coinliquidity.core.model.Exchange;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class HttpDownloaderTest {

    private HttpDownloader httpDownloader = new HttpDownloader(null);

    @Test
    public void getUrl_lower() {
        final Exchange exchange = new Exchange();
        exchange.setUrl("https://www.example.com?ccy=<base>-<quote>");

        final String actual = httpDownloader.getUrl(exchange, CurrencyPair.BTC_USD);

        assertThat(actual, equalTo("https://www.example.com?ccy=btc-usd"));
    }

    @Test
    public void getUrl_upper() {
        final Exchange exchange = new Exchange();
        exchange.setUrl("https://www.example.com?ccy=<BASE>-<QUOTE>");

        final String actual = httpDownloader.getUrl(exchange, CurrencyPair.BTC_USD);

        assertThat(actual, equalTo("https://www.example.com?ccy=BTC-USD"));
    }

    @Test
    public void getUrl_maxDepth() {
        final Exchange exchange = new Exchange();
        exchange.setUrl("https://www.example.com?max=<MAX_DEPTH>");
        exchange.setMaxDepth(100);

        final String actual = httpDownloader.getUrl(exchange, CurrencyPair.BTC_USD);

        assertThat(actual, equalTo("https://www.example.com?max=100"));
    }

}