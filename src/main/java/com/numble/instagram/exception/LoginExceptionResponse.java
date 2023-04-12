package com.numble.instagram.exception;

public class LoginExceptionResponse extends RuntimeException {

    public LoginExceptionResponse() {
    }

    public LoginExceptionResponse(String message) {
        super(message);
    }
}
