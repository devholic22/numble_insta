package com.numble.instagram.exception;

public class ExitedTargetUserException extends RuntimeException {

    public ExitedTargetUserException() {
    }

    public ExitedTargetUserException(String message) {
        super(message);
    }
}
