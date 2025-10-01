package com.ippclub.ippdicebackend.exception;

public class ResourceNotFoundException extends RuntimeException {
    private final int code;

    public ResourceNotFoundException(String message) {
        super(message);
        this.code = 404;
    }

    public int getCode() {
        return code;
    }
}