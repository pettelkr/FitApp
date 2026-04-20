package com.fitapp.model;

public class CalorieLimitExceededException extends IllegalStateException {
    public CalorieLimitExceededException() {
        super("Daily calorie limit exceeded");
    }
}
