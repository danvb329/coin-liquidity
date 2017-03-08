package com.coinliquidity.core.parsers;

import com.coinliquidity.core.Parser;
import com.coinliquidity.core.model.CurrencyPair;
import com.coinliquidity.core.model.OrderBook;
import com.coinliquidity.core.model.Orders;
import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;

public class ArrayParser implements Parser {

    public ArrayParser() {
    }

    public OrderBook parse(final String exchange, final CurrencyPair currencyPair, final JsonNode json) {
        JsonNode bids = json.findValue("bids");
        JsonNode asks = json.findValue("asks");

        if (bids == null) {
            bids = json.findValue("buy");
        }

        if (asks == null) {
            asks = json.findValue("sell");
        }

        if (bids == null) {
            throw new OrderBookParseException("bids not found in json");
        }

        if (asks == null) {
            throw new OrderBookParseException("asks not found in json");
        }

        return new OrderBook(exchange, currencyPair, toOrders(bids), toOrders(asks));
    }

    private Orders toOrders(JsonNode nodes) {
        final Orders retVal = new Orders();
        for (JsonNode node : nodes) {
            retVal.put(new BigDecimal(node.get(0).asText()),
                    new BigDecimal(node.get(1).asText()));
        }
        return retVal;
    }
}
