package com.fitapp.model;

import java.util.Date;

public class CardioCalisthenicsExercise extends Exercise {

    // -------------------------
    // ATTRIBUTES
    // -------------------------

    private double interval;
    private int numOfExercisesPerRound;
    private int round;


    // -------------------------
    // CONSTRUCTOR
    // -------------------------

    public CardioCalisthenicsExercise(
            int id,
            String name,
            String description,
            Date date,
            String difficulty,
            double duration,
            double calories,
            double interval,
            int numOfExercisesPerRound,
            int round) {

        super(
                id,
                name,
                description,
                date,
                difficulty,
                duration,
                calories
        );

        this.interval = interval;
        this.numOfExercisesPerRound = numOfExercisesPerRound;
        this.round = round;
    }


    // -------------------------
    // GETTERS
    // -------------------------

    public double getInterval() {
        return interval;
    }

    public int getNumOfExercisesPerRound() {
        return numOfExercisesPerRound;
    }

    public int getRound() {
        return round;
    }


    // -------------------------
    // OTHER METHODS
    // -------------------------

    public int getTotalNumOfEx(
            int numOfExercisesPerRound,
            int round) {

        return numOfExercisesPerRound * round;
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
