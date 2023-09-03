package com.msomodi.beersapi;

public class BeerApiException extends RuntimeException {

    public BeerApiException(String message) {
        super(message);
    }

    public BeerApiException(String message, Throwable cause) {
        super(message, cause);
    }
}