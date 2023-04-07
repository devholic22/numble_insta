package com.numble.instagram.exception;

public class AlreadyExitedUserException extends RuntimeException {

    public AlreadyExitedUserException() {
    }

    public AlreadyExitedUserException(String message) {
        super(message);
    }
}
