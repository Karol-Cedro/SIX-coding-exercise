package com.kcedro.exceptions;

public class MissionNotFoundException extends RuntimeException {
    public MissionNotFoundException(String message) {
        super(message);
    }
}
