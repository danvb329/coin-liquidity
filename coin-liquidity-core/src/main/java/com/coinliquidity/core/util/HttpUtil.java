package com.coinliquidity.core.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class HttpUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static JsonNode get(final String url) {
        final Client client = ClientBuilder.newClient();
        client.property(ClientProperties.CONNECT_TIMEOUT, 10000);
        client.property(ClientProperties.READ_TIMEOUT,    20000);

        final WebTarget target = client.target(url);
        final Response response = target.request().get();
        try {
            final InputStream is = response.readEntity(InputStream.class);
            return readTree(is, isGzip(response));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isGzip(final Response response) {
        final String contentEncoding = response.getHeaderString("Content-Encoding");
        return contentEncoding != null && contentEncoding.equals("gzip");
    }

    private static JsonNode readTree(final InputStream is, boolean gzip) throws IOException {
        if (gzip) {
            return MAPPER.readTree(new GZIPInputStream(is));
        } else {
            return MAPPER.readTree(is);
        }
    }
}
