package com.coinliquidity.core.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Objects;

@JsonDeserialize(using = CurrencyPairDeserializer.class)
@JsonSerialize(using = CurrencyPairSerializer.class)
public class CurrencyPair {

    public static final String BTC = "BTC";

    private static final Map<String, String> CCY_NORMALIZATION;

    static {
        CCY_NORMALIZATION = Maps.newHashMap();
        CCY_NORMALIZATION.put("XBT", BTC);
        CCY_NORMALIZATION.put("RUR", "RUB");
        CCY_NORMALIZATION.put("USDT", "USD");
        CCY_NORMALIZATION.put("DSH", "DASH");
    }

    private final String baseCurrency;
    private final String quoteCurrency;

    public CurrencyPair(final String baseCurrency, final String quoteCurrency) {
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getQuoteCurrency() {
        return quoteCurrency;
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
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CurrencyPair that = (CurrencyPair) o;
        return Objects.equals(baseCurrency, that.baseCurrency) &&
                Objects.equals(quoteCurrency, that.quoteCurrency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseCurrency, quoteCurrency);
    }

    @Override
    public String toString() {
        return baseCurrency + "/" + quoteCurrency;
    }
}
