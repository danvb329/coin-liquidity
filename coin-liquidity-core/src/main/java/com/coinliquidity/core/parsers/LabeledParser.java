package com.coinliquidity.core.parsers;

import com.coinliquidity.core.Parser;
import com.coinliquidity.core.model.CurrencyPair;
import com.coinliquidity.core.model.OrderBook;
import com.coinliquidity.core.model.Orders;
import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;

public class LabeledParser implements Parser {

    public OrderBook parse(final String exchange, final CurrencyPair currencyPair, final JsonNode json) {
        final JsonNode bids = json.get("bids");
        final JsonNode asks = json.get("asks");

        return new OrderBook(exchange, currencyPair, toOrders(bids), toOrders(asks));
    }

    private Orders toOrders(JsonNode nodes) {
        final Orders retVal = new Orders();
        for (JsonNode node : nodes) {
            retVal.put(new BigDecimal(node.get("price").asText()),
                    new BigDecimal(node.get("amount").asText()));
        }
        return retVal;
    }
}
