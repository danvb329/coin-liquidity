package com.coinliquidity.core;

import com.coinliquidity.core.model.Exchanges;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

public class ExchangeConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeConfig.class);

    private final String location;
    private final ObjectMapper mapper;

    public ExchangeConfig(final String location) {
        this.location = location;
        this.mapper = new ObjectMapper();
    }

    public Exchanges loadExchanges() {
        try {
            final URL url;
            if (location.startsWith("http")) {
                LOGGER.info("Getting config from url: {}", location);
                url = new URL(location);
            } else {
                LOGGER.info("Getting config from resource file: {}", location);
                url = Resources.getResource(location);
            }
            return mapper.readValue(url, Exchanges.class);
        } catch (IOException e) {
            throw new RuntimeException("Could not read exchange config", e);
        }
    }
}
