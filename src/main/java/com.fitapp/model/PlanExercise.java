package com.fitapp.model;

public class PlanExercise {

    // -------------------------
    // ATTRIBUTES
    // -------------------------

    private Exercise exercise;

    // Dauer dieser Übung innerhalb des Plans
    private double duration;

    // Optional
    private int sets;

    // Optional
    private int reps;


    // -------------------------
    // CONSTRUCTOR
    // -------------------------

    public PlanExercise(
            Exercise exercise,
            double duration,
            int sets,
            int reps) {

        this.exercise = exercise;
        this.duration = duration;
        this.sets = sets;
        this.reps = reps;
    }


    // -------------------------
    // GETTERS
    // -------------------------

    public Exercise getExercise() {
        return exercise;
    }

    public double getDuration() {
        return duration;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }


    // -------------------------
    // SETTERS
    // -------------------------

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }


    // -------------------------
    // CALCULATIONS
    // -------------------------

    public double getCalories() {

        if (exercise == null || duration <= 0) {
            return 0;
        }

        /*
         * Die Exercise speichert:
         *
         * calories = kcal pro Stunde
         *
         * duration = Minuten im Trainingsplan
         *
         * Beispiel:
         *
         * 600 kcal/h
         * 30 Minuten
         *
         * = 600 * 30 / 60
         * = 300 kcal
         */

        return exercise.getCalories() * duration / 60.0;
    }


    // -------------------------
    // DISPLAY
    // -------------------------

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();

        result.append(exercise.getName());

        result.append(" - ");
        result.append(duration);
        result.append(" min");

        if (sets > 0) {
            result.append(" - ");
            result.append(sets);
            result.append(" sets");
        }

        if (reps > 0) {
            result.append(" - ");
            result.append(reps);
            result.append(" reps");
        }

        return result.toString();
    }
}
