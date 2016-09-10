package com.coinliquidity.core;

import com.coinliquidity.core.model.CurrencyPair;
import com.coinliquidity.core.model.OrderBook;
import com.fasterxml.jackson.databind.JsonNode;

public interface Parser {

    OrderBook parse(final String exchange, final CurrencyPair currencyPair, final JsonNode node);
}
