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

    private Orders toOrders(final JsonNode nodes) {
        final Orders retVal = new Orders();
        for (final JsonNode node : nodes) {
            final String price = node.path("price").asText();
            String amount = node.path("amount").asText();
            if (amount == null || amount.trim().length() == 0) {
                amount = node.get("size").asText();
            }
            retVal.put(new BigDecimal(price), new BigDecimal(amount));
        }
        return retVal;
    }
}
