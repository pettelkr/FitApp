package com.fitapp.model;

import java.util.Date;

public class CardioRunningExercise extends Exercise {

    // -------------------------
    // ATTRIBUTES
    // -------------------------

    private double distance;
    private double speed;
    private int steps;


    // -------------------------
    // CONSTRUCTOR
    // -------------------------

    public CardioRunningExercise(
            int id,
            String name,
            String description,
            Date date,
            String difficulty,
            double duration,
            double calories,
            double distance,
            double speed,
            int steps) {

        super(
                id,
                name,
                description,
                date,
                difficulty,
                duration,
                calories
        );

        this.distance = distance;
        this.speed = speed;
        this.steps = steps;
    }


    // -------------------------
    // GETTERS
    // -------------------------

    public double getDistance() {
        return distance;
    }

    public double getSpeed() {
        return speed;
    }

    public int getSteps() {
        return steps;
    }


    // -------------------------
    // CALCULATIONS
    // -------------------------

    @Override
    public double calcCalories() {

        return calculateCaloriesForDuration(
                getDuration()
        );
    }
}
