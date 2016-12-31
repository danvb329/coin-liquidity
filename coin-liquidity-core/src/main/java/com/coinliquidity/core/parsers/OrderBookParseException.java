package com.coinliquidity.core.parsers;

public class OrderBookParseException extends RuntimeException {

    public OrderBookParseException(String message) {
        super(message);
    }

    public OrderBookParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
