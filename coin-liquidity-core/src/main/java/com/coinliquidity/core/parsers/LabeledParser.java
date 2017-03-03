package com.coinliquidity.core.parsers;

import com.coinliquidity.core.Parser;
import com.coinliquidity.core.model.CurrencyPair;
import com.coinliquidity.core.model.OrderBook;
import com.coinliquidity.core.model.Orders;
import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;

public class LabeledParser implements Parser {

    public OrderBook parse(final String exchange, final CurrencyPair currencyPair, final JsonNode json) {
        final JsonNode bids = path(json, "bids", "bid");
        final JsonNode asks = path(json, "asks", "ask");

        if (bids == null) {
            throw new OrderBookParseException("bids not found in json");
        }

        if (asks == null) {
            throw new OrderBookParseException("asks not found in json");
        }

        return new OrderBook(exchange, currencyPair, toOrders(bids), toOrders(asks));
    }

    private Orders toOrders(final JsonNode nodes) {
        final Orders retVal = new Orders();
        for (final JsonNode node : nodes) {
            final JsonNode price = path(node, "price");
            final JsonNode amount = path(node, "amount", "size", "qty");
            if (amount == null) {
                throw new RuntimeException("Could not determine amount");
            }
            if (price == null) {
                throw new RuntimeException("Could not determine price");
            }
            retVal.put(new BigDecimal(price.asText()), new BigDecimal(amount.asText()));
        }
        return retVal;
    }

    private JsonNode path(JsonNode parent, final String... possibleNames) {
        for (final String name : possibleNames) {
            final JsonNode node = parent.path(name);
            if (node != null && !node.isMissingNode()) {
                return node;
            }
        }
        return null;
    }
}
