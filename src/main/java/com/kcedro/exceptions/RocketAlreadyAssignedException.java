package com.kcedro.exceptions;

public class RocketAlreadyAssignedException extends RuntimeException {
    public RocketAlreadyAssignedException(String message) {
        super(message);
    }
}
