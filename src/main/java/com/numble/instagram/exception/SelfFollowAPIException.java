package com.numble.instagram.exception;

public class SelfFollowAPIException extends RuntimeException {

    public SelfFollowAPIException() {
    }

    public SelfFollowAPIException(String message) {
        super(message);
    }
}
