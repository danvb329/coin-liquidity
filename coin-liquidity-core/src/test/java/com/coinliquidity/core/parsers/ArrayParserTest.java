package com.coinliquidity.core.parsers;

import com.coinliquidity.core.model.CurrencyPair;
import com.coinliquidity.core.model.OrderBook;
import com.coinliquidity.core.model.Orders;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class ArrayParserTest {

    private ArrayParser arrayParser;

    @Before
    public void setUp() {
        arrayParser = new ArrayParser();
    }

    @Test
    public void test() throws IOException {
        String json = Resources.toString(Resources.getResource("com/coinliquidity/core/parsers/array.json"), Charsets.UTF_8);
        JsonNode node = new ObjectMapper().readTree(json);
        OrderBook orderBook = arrayParser.parse("exchange", new CurrencyPair("BTC", "USD"), node);

        final Orders bids = new Orders();
        bids.put(new BigDecimal("100"), new BigDecimal("1.1"));
        bids.put(new BigDecimal("200"), new BigDecimal("2.2"));
        bids.put(new BigDecimal("300"), new BigDecimal("3.3"));
        final Orders asks = new Orders();
        asks.put(new BigDecimal("400"), new BigDecimal("4.4"));
        asks.put(new BigDecimal("500"), new BigDecimal("5.5"));
        asks.put(new BigDecimal("600"), new BigDecimal("6.6"));

        final OrderBook expected = new OrderBook("exchange", new CurrencyPair("BTC", "USD"), bids, asks);
        assertEquals(expected, orderBook);

    }
}
