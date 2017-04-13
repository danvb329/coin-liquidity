package com.coinliquidity.core.parsers;

import com.coinliquidity.core.Parser;
import com.coinliquidity.core.model.CurrencyPair;
import com.coinliquidity.core.model.OrderBook;
import com.coinliquidity.core.model.Orders;
import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;

import static com.coinliquidity.core.parsers.ParseUtils.checkNotNull;
import static com.coinliquidity.core.parsers.ParseUtils.path;

public abstract class AbstractParser implements Parser {

    @Override
    public OrderBook parse(final String exchange, final CurrencyPair currencyPair, final JsonNode json) {
        final JsonNode bids = path(json, "bids", "bid", "buy");
        final JsonNode asks = path(json, "asks", "ask", "sell");

        checkNotNull(bids, "bids not found in json");
        checkNotNull(asks, "asks not found in json");

        final OrderBook orderBook = new OrderBook(exchange, currencyPair);
        addOrders(bids, orderBook.getBids());
        addOrders(asks, orderBook.getAsks());
        return orderBook;
    }

    private void addOrders(final JsonNode nodes, final Orders orders) {
        for (final JsonNode orderNode : nodes) {
            final JsonNode price = getPrice(orderNode);
            final JsonNode units = getUnits(orderNode);

            checkNotNull(units, "could not determine units");
            checkNotNull(price, "could not determine price");

            orders.put(new BigDecimal(price.asText()), new BigDecimal(units.asText()));
        }
    }

    abstract JsonNode getPrice(final JsonNode orderNode);
    abstract JsonNode getUnits(final JsonNode orderNode);
}
