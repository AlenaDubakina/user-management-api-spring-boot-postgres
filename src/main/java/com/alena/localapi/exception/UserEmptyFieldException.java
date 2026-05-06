package com.alena.localapi.exception;

public class UserEmptyFieldException extends RuntimeException {
    private final String field;
    private final String message;

    public UserEmptyFieldException(String field, String message) {
        super(message);
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
