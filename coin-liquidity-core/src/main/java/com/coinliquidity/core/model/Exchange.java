package com.coinliquidity.core.model;

import com.coinliquidity.core.Parser;

import java.util.List;

public class Exchange {

    private String name;
    private String url;
    private ParserType parserType;
    private List<CurrencyPair> currencies;
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

    // TODO - remove this
    public List<CurrencyPair> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(final List<CurrencyPair> currencies) {
        this.currencies = currencies;
    }

    public List<CurrencyPair> getCurrencyPairs() {
        return currencies;
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
