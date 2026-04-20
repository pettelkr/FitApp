package com.fitapp.model;

public class NegativeCaloriesException extends IllegalArgumentException {
    public NegativeCaloriesException() {
        super("Calories must be positive");
    }
}
