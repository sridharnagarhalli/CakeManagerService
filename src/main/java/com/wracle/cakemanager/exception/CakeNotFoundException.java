package com.wracle.cakemanager.exception;

public class CakeNotFoundException extends RuntimeException {
    public CakeNotFoundException(final String message) {
        super(message);
    }
}
