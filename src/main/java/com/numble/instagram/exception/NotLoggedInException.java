package com.numble.instagram.exception;

public class NotLoggedInException extends RuntimeException {

    public NotLoggedInException() {
    }

    public NotLoggedInException(String message) {
        super(message);
    }
}
