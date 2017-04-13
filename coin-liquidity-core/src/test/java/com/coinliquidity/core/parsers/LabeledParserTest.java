package com.coinliquidity.core.parsers;

import com.coinliquidity.core.model.CurrencyPair;
import com.coinliquidity.core.model.OrderBook;
import com.coinliquidity.core.model.Orders;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static com.coinliquidity.core.util.ResourceUtils.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class LabeledParserTest {

    private LabeledParser parser;

    @Before
    public void setUp() {
        parser = new LabeledParser();
    }

    @Test
    public void test() {
        final JsonNode node = json("com/coinliquidity/core/parsers/labeled.json");

        final OrderBook orderBook = parser.parse("exchange", CurrencyPair.BTC_USD, node);

        final OrderBook expected = new OrderBook("exchange", CurrencyPair.BTC_USD);
        final Orders bids = expected.getBids();
        bids.put(new BigDecimal("100"), new BigDecimal("1.1"));
        bids.put(new BigDecimal("200"), new BigDecimal("2.2"));
        bids.put(new BigDecimal("300"), new BigDecimal("3.3"));
        final Orders asks = expected.getAsks();
        asks.put(new BigDecimal("400"), new BigDecimal("4.4"));
        asks.put(new BigDecimal("500"), new BigDecimal("5.5"));
        asks.put(new BigDecimal("600"), new BigDecimal("6.6"));

        assertEquals(expected, orderBook);
    }

    @Test
    public void noPrice() {
        final JsonNode json = json("com/coinliquidity/core/parsers/labeled_no_price.json");

        try {
            parser.parse("exchange", CurrencyPair.BTC_USD, json);
            fail("expected OrderBookParseException");
        } catch (final OrderBookParseException e) {
            assertEquals("could not determine price", e.getMessage());
        }
    }

    @Test
    public void noUnits() {
        final JsonNode json = json("com/coinliquidity/core/parsers/labeled_no_units.json");

        try {
            parser.parse("exchange", CurrencyPair.BTC_USD, json);
            fail("expected OrderBookParseException");
        } catch (final OrderBookParseException e) {
            assertEquals("could not determine units", e.getMessage());
        }
    }
}
