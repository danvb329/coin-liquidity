package com.coinliquidity.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class IllegalFilterException extends RuntimeException {

    public IllegalFilterException(String message) {
        super(message);
    }
}
