package com.coinliquidity.core.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import lombok.SneakyThrows;

public class ResourceUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @SneakyThrows
    public static String resource(final String name) {
        return Resources.toString(Resources.getResource(name), Charsets.UTF_8);
    }

    @SneakyThrows
    public static JsonNode json(final String name) {
        return MAPPER.readTree(resource(name));
    }
}
