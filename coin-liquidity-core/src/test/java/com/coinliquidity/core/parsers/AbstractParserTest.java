package com.coinliquidity.core.parsers;

import com.coinliquidity.core.model.CurrencyPair;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Before;
import org.junit.Test;

import static com.coinliquidity.core.util.ResourceUtils.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AbstractParserTest {

    private AbstractParser parser;

    @Before
    public void setUp() {
        parser = new AbstractParser() {
            @Override
            JsonNode getPrice(final JsonNode orderNode) {
                return null;
            }

            @Override
            JsonNode getUnits(final JsonNode orderNode) {
                return null;
            }
        };
    }

    @Test
    public void noBids() {
        final JsonNode json = json("com/coinliquidity/core/parsers/no_bids.json");

        try {
            parser.parse("exchange", CurrencyPair.BTC_USD, json);
            fail("expected OrderBookParseException");
        } catch (final OrderBookParseException e) {
            assertEquals("bids not found in json", e.getMessage());
        }
    }

    @Test
    public void noAsks() {
        final JsonNode json = json("com/coinliquidity/core/parsers/no_asks.json");

        try {
            parser.parse("exchange", CurrencyPair.BTC_USD, json);
            fail("expected OrderBookParseException");
        } catch (final OrderBookParseException e) {
            assertEquals("asks not found in json", e.getMessage());
        }
    }

}
