package com.numble.instagram.exception;

public class NotFollowException extends RuntimeException {

    public NotFollowException() {
    }

    public NotFollowException(String message) {
        super(message);
    }
}
