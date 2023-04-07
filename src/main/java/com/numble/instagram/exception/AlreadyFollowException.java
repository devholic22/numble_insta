package com.numble.instagram.exception;

public class AlreadyFollowException extends RuntimeException {

    public AlreadyFollowException() {
    }

    public AlreadyFollowException(String message) {
        super(message);
    }
}
