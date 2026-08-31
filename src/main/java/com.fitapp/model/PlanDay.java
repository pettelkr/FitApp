package com.fitapp.model;

import java.util.ArrayList;
import java.util.List;

public class PlanDay {

    // -------------------------
    // ATTRIBUTES
    // -------------------------

    private String dayName;

    private List<PlanExercise> exercises;


    // -------------------------
    // CONSTRUCTOR
    // -------------------------

    public PlanDay(String dayName) {

        this.dayName = dayName;
        this.exercises = new ArrayList<>();
    }


    // -------------------------
    // GETTERS
    // -------------------------

    public String getDayName() {
        return dayName;
    }

    public List<PlanExercise> getExercises() {
        return exercises;
    }


    // -------------------------
    // ADD EXERCISE
    // -------------------------

    public void addExercise(PlanExercise exercise) {

        if (exercise != null) {
            exercises.add(exercise);
        }
    }


    // -------------------------
    // REMOVE EXERCISE
    // -------------------------

    public void removeExercise(int exerciseId) {

        exercises.removeIf(
                planExercise ->
                        planExercise.getExercise().getId() == exerciseId
        );
    }


    // -------------------------
    // GET EXERCISE
    // -------------------------

    public PlanExercise getExerciseById(int id) {

        for (PlanExercise planExercise : exercises) {

            if (planExercise.getExercise().getId() == id) {
                return planExercise;
            }
        }

        return null;
    }


    // -------------------------
    // CALCULATIONS
    // -------------------------

    public double getTotalCalories() {

        double total = 0;

        for (PlanExercise exercise : exercises) {

            total += exercise.getCalories();
        }

        return total;
    }


    public double getTotalDuration() {

        double total = 0;

        for (PlanExercise exercise : exercises) {

            total += exercise.getDuration();
        }

        return total;
    }


    // -------------------------
    // NUMBER OF EXERCISES
    // -------------------------

    public int getNumberOfExercises() {

        return exercises.size();
    }


    // -------------------------
    // UTILITY
    // -------------------------

    public boolean isEmpty() {

        return exercises.isEmpty();
    }


    public void clearExercises() {

        exercises.clear();
    }


    // -------------------------
    // DISPLAY
    // -------------------------

    @Override
    public String toString() {

        return dayName;
    }
}
