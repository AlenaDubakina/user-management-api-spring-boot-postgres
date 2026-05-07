package com.alena.localapi.exception;

public class UserEmptyFieldException extends RuntimeException {
    private final String field;

    public UserEmptyFieldException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}