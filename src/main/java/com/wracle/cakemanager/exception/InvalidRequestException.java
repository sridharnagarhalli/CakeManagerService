package com.wracle.cakemanager.exception;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(final String message) {
        super(message);
    }
}
