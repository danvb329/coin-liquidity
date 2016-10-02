package com.coinliquidity.core.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

class CurrencyPairDeserializer extends JsonDeserializer<CurrencyPair> {
    @Override
    public CurrencyPair deserialize(final JsonParser jsonParser,
                                    final DeserializationContext deserializationContext) throws IOException {
        final JsonNode node = jsonParser.readValueAsTree();
        final String[] fields = node.asText().split("/");
        return new CurrencyPair(fields[0], fields[1]);
    }
}
