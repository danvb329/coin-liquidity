package com.coinliquidity.core.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

class CurrencyPairSerializer extends JsonSerializer<CurrencyPair> {

    @Override
    public void serialize(final CurrencyPair currencyPair, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(currencyPair.toString());
    }
}