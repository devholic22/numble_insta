package com.numble.instagram.exception;

public class NotPermissionException extends RuntimeException {

    public NotPermissionException() {
    }

    public NotPermissionException(String message) {
        super(message);
    }
}
