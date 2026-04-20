package com.fitapp.model;

public class InvalidCredentialsException extends SecurityException {
    public InvalidCredentialsException() {
        super("Invalid username or password");
    }
}
