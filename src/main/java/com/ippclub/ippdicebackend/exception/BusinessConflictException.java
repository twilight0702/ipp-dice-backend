package com.ippclub.ippdicebackend.exception;

public class BusinessConflictException extends RuntimeException {
    private final int code;

    public BusinessConflictException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
