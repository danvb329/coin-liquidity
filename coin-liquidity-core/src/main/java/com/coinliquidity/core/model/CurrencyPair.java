package com.coinliquidity.core.model;

import java.util.Objects;

public class CurrencyPair {
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
        switch(currency) {
            case "XBT":
                return "BTC";
            case "RUR":
                return "RUB";
            default:
                return currency;
        }
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
