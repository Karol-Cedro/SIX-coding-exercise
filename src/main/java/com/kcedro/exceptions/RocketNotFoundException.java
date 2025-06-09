package com.kcedro.exceptions;

public class RocketNotFoundException extends RuntimeException {
    public RocketNotFoundException(String message) {
        super(message);
    }
}
