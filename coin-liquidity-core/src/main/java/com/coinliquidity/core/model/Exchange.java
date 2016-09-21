package com.coinliquidity.core.model;

import com.coinliquidity.core.Parser;

import java.util.ArrayList;
import java.util.List;

public class Exchange {

    private String name;
    private String url;
    private ParserType parserType;
    private List<String> currencies;
    private List<CurrencyPair> currencyPairs;
    private String docUrl;
    private Integer rateLimit = 1;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public ParserType getParserType() {
        return parserType;
    }

    public void setParserType(final ParserType parserType) {
        this.parserType = parserType;
    }

    public List<String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(final List<String> currencies) {
        this.currencies = currencies;
    }

    public List<CurrencyPair> getCurrencyPairs() {
        final List<CurrencyPair> currencyPairs = new ArrayList<>(currencies.size());
        for (final String currency : currencies) {
            final String[] values = currency.split("/");
            final String baseCurrency = values[0];
            final String quoteCurrency = values[1];

            // skip commented-out exchanges
            if (!baseCurrency.startsWith("--")) {
                currencyPairs.add(new CurrencyPair(baseCurrency, quoteCurrency));
            }
        }
        return currencyPairs;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(final String docUrl) {
        this.docUrl = docUrl;
    }

    /**
     * Maximum calls per second (default = 1)
     * @return the maximum calls per second
     */
    public Integer getRateLimit() {
        return rateLimit;
    }

    public void setRateLimit(Integer rateLimit) {
        this.rateLimit = rateLimit;
    }

    public Parser getParser() {
        return parserType.getParser();
    }

    @Override
    public String toString() {
        return name;
    }
}
