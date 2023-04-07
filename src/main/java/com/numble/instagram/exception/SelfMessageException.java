package com.numble.instagram.exception;

public class SelfMessageException extends RuntimeException {

    public SelfMessageException() {
    }

    public SelfMessageException(String message) {
        super(message);
    }
}
