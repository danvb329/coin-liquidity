package com.coinliquidity.core;

import com.coinliquidity.core.model.Exchanges;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;

import java.io.IOException;

public class ExchangeConfig {

    private final String filename;

    public ExchangeConfig(final String filename) {
        this.filename = filename;
    }

    public Exchanges loadExchanges() {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Resources.getResource(filename), Exchanges.class);
        } catch (IOException e) {
            throw new RuntimeException("Could not read exchange config", e);
        }
    }
}
