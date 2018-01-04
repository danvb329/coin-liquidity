package com.coinliquidity.core.model;

import lombok.Data;

import java.util.List;

@Data
public class Exchange {

    private String name;
    private String url;
    private ParserType parserType;
    private List<CurrencyPair> currencies;
    private String docUrl;
    private Integer rateLimit = 1;
    private Integer maxDepth;
    private String location;

    @Override
    public String toString() {
        return name;
    }
}
