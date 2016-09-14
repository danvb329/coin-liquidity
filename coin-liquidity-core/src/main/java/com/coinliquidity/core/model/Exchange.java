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
            currencyPairs.add(new CurrencyPair(values[0], values[1]));
        }
        return currencyPairs;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(final String docUrl) {
        this.docUrl = docUrl;
    }

    @Override
    public String toString() {
        return name;
    }

    public Parser getParser() {
        return parserType.getParser();
    }
}
