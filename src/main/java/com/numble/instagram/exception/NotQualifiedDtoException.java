package com.numble.instagram.exception;

public class NotQualifiedDtoException extends RuntimeException {

    public NotQualifiedDtoException() {
    }

    public NotQualifiedDtoException(String message) {
        super(message);
    }
}
