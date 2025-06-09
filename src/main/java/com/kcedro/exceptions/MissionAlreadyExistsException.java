package com.kcedro.exceptions;

public class MissionAlreadyExistsException extends RuntimeException {
    public MissionAlreadyExistsException(String message) {
        super(message);
    }
}
