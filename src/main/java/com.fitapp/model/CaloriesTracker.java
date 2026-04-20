package com.fitapp.model;

import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class CaloriesTracker {

    /**
     * This class uses Java Property classes to implement and take advantage of the observer pattern.
     * The observer pattern is a design pattern in which an object, known as the subject,
     * maintains a list of its dependents, known as observers, and notifies them automatically of any state changes.
     * These property classes are observers under the hood.
     *
     * Why do we use this:
     * Loose coupling: Observers are notified of state changes without needing to know anything about the subject
     * This makes it easy to add, remove, or modify observers without affecting the subject
     *
     * The observer pattern is highly flexible and can be used in a wide range of software applications.
     *
     * The observer pattern establishes a one-to-many relationship. We have one subject and multiple observers.
     * The subject in this scenario is the CaloriesTracker class more specifically its attributes (observable states).
     * The observers are anything which listens or binds to these attributes. So if the attributes ever change they
     * notify the observers.
     */

    // attributes
    private final IntegerProperty consumed = new SimpleIntegerProperty(0);
    private final IntegerProperty dailyLimit;

    // constructor
    public CaloriesTracker(int dailyLimit) {
        this.dailyLimit = new SimpleIntegerProperty(dailyLimit);
    }

    // getters and setters


    public IntegerProperty getDailyLimit() {
        return dailyLimit;
    }
    public IntegerProperty getConsumed(){return consumed;}

    // methods
    public void addCalories(int calories) {
        if (calories < 0) {
            throw new NegativeCaloriesException();
        }
        if (consumed.get() + calories > dailyLimit.get()) {
            throw new CalorieLimitExceededException();
        }
        consumed.set(consumed.get() + calories);
    }

    public void reset() {
        consumed.set(0);
    }

    public IntegerBinding remainingCaloriesProperty() {
        return (IntegerBinding) dailyLimit.subtract(consumed);
    }
}
