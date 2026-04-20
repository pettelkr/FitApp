package com.fitapp.model;

public class EmptyFieldException extends IllegalArgumentException {
    public EmptyFieldException(String fieldName) {
        super(fieldName + " must not be empty");
    }
}
