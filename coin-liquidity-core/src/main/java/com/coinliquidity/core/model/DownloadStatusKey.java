package com.coinliquidity.core.model;

import com.google.common.collect.ComparisonChain;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DownloadStatusKey implements Comparable<DownloadStatusKey> {
    private String exchangeName;
    private CurrencyPair currencyPair;

    @Override
    public int compareTo(final DownloadStatusKey that) {
        return ComparisonChain.start()
                .compare(this.exchangeName, that.exchangeName)
                .compare(this.currencyPair, that.currencyPair)
                .result();
    }
}
