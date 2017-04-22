package com.coinliquidity.core.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

@JsonDeserialize(using = CurrencyPairDeserializer.class)
@JsonSerialize(using = CurrencyPairSerializer.class)
@Data
public class CurrencyPair implements Comparable<CurrencyPair> {

    public static final String BTC = "BTC";
    public static final String USD = "USD";

    public static final CurrencyPair BTC_USD = new CurrencyPair(BTC, USD);

    private static final Map<String, String> CCY_NORMALIZATION;

    static {
        CCY_NORMALIZATION = Maps.newHashMap();
        CCY_NORMALIZATION.put("XBT", BTC);
        CCY_NORMALIZATION.put("RUR", "RUB");
        CCY_NORMALIZATION.put("USDT", USD);
        CCY_NORMALIZATION.put("DSH", "DASH");
    }

    private final String baseCurrency;
    private final String quoteCurrency;

    public CurrencyPair(final String baseCurrency, final String quoteCurrency) {
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
    }

    /**
     * Normalize weird currencies to common codes
     * @return the normalized currency pair
     */
    public CurrencyPair normalize() {
        return new CurrencyPair(normalize(baseCurrency), normalize(quoteCurrency));
    }

    private String normalize(final String currency) {
        return CCY_NORMALIZATION.getOrDefault(currency, currency);
    }

    public boolean isDisabled() {
        return baseCurrency.startsWith("--");
    }

    @Override
    public String toString() {
        return baseCurrency + "/" + quoteCurrency;
    }

    @Override
    public int compareTo(final CurrencyPair that) {
        return ComparisonChain.start()
                .compare(this.baseCurrency, that.baseCurrency)
                .compare(this.quoteCurrency, that.quoteCurrency)
                .result();
    }
}
