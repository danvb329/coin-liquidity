package com.coinliquidity.core.parsers;

import com.fasterxml.jackson.databind.JsonNode;

public class ArrayParser extends AbstractParser {

    @Override
    JsonNode getPrice(final JsonNode orderNode) {
        return orderNode.get(0);
    }

    @Override
    JsonNode getUnits(final JsonNode orderNode) {
        return orderNode.get(1);
    }
}
