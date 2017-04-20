package com.coinliquidity.core.download;

import com.coinliquidity.core.model.CurrencyPair;
import com.coinliquidity.core.model.Exchange;
import com.coinliquidity.core.model.OrderBook;

public interface Downloader {

    OrderBook download(final Exchange exchange, final CurrencyPair currencyPair);

    String getUrl(final Exchange exchange, final CurrencyPair currencyPair);
}
