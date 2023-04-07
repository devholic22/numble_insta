package com.numble.instagram.exception;

public class AlreadyExistUserException extends RuntimeException {

    public AlreadyExistUserException() {
    }

    public AlreadyExistUserException(String message) {
        super(message);
    }
}
