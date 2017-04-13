package com.coinliquidity.core.parsers;

import com.fasterxml.jackson.databind.JsonNode;

class ParseUtils {

    static JsonNode path(JsonNode parent, final String... possibleNames) {
        for (final String name : possibleNames) {
            final JsonNode node = parent.findValue(name);
            if (node != null && !node.isMissingNode()) {
                return node;
            }
        }
        return null;
    }

    static void checkNotNull(final Object o, final String message) {
        if (o == null) {
            throw new OrderBookParseException(message);
        }
    }
}
