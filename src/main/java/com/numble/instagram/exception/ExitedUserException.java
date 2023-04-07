package com.numble.instagram.exception;

public class ExitedUserException extends RuntimeException {

    public ExitedUserException() {
    }

    public ExitedUserException(String message) {
        super(message);
    }
}
