package com.fitapp.model;

import java.util.Date;

public abstract class Exercise {

    // -------------------------
    // ATTRIBUTES
    // -------------------------

    private int id;
    private String name;
    private String description;
    private Date date;
    private String difficulty;

    // Dauer der Übung in Minuten
    private double duration;

    // Kalorien pro Stunde
    private double calories;


    // -------------------------
    // CONSTRUCTOR
    // -------------------------

    public Exercise(
            int id,
            String name,
            String description,
            Date date,
            String difficulty,
            double duration,
            double calories) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.difficulty = difficulty;
        this.duration = duration;
        this.calories = calories;
    }


    // -------------------------
    // GETTERS
    // -------------------------

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Dauer der Übung in Minuten.
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Kalorienverbrauch pro Stunde.
     */
    public double getCalories() {
        return calories;
    }


    // -------------------------
    // SETTERS
    // -------------------------

    public void setDuration(double duration) {
        this.duration = duration;
    }


    // -------------------------
    // CALCULATIONS
    // -------------------------

    /**
     * Berechnet den tatsächlichen Kalorienverbrauch
     * für die eingestellte Dauer.
     *
     * Beispiel:
     *
     * 600 kcal/h
     * 30 Minuten
     *
     * = 600 * 30 / 60
     * = 300 kcal
     */
    public double calculateCaloriesForDuration(double duration) {

        if (duration <= 0) {
            return 0;
        }

        return calories * duration / 60.0;
    }


    /**
     * Berechnet die Kalorien für die aktuell
     * eingestellte Dauer der Übung.
     */
    public abstract double calcCalories();


    // -------------------------
    // SAVE
    // -------------------------

    public void save() {
        // to be implemented
    }


    // -------------------------
    // COMBOBOX DISPLAY
    // -------------------------

    @Override
    public String toString() {
        return name;
    }
}
