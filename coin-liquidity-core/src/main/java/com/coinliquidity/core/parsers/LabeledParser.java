package com.coinliquidity.core.parsers;

import com.fasterxml.jackson.databind.JsonNode;

import static com.coinliquidity.core.parsers.ParseUtils.path;

public class LabeledParser extends AbstractParser {

    @Override
    JsonNode getPrice(final JsonNode orderNode) {
        return path(orderNode, "price");
    }

    @Override
    JsonNode getUnits(final JsonNode orderNode) {
        return path(orderNode, "amount", "size", "qty", "volume");
    }
}
