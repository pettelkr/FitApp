package com.fitapp.model;

import java.util.ArrayList;
import java.util.List;

public class ExerciseService {

    // Liste mit allen erstellten Übungen
    private static final List<Exercise> exercises = new ArrayList<>();

    // ID für neue Übungen
    private static int nextId = 1;


// -------------------------
// ADD EXERCISE
// -------------------------

    public Exercise addExercise(Exercise exercise) {

        if (exercise == null) {
            return null;
        }

        exercises.add(exercise);

        return exercise;
    }


// -------------------------
// GET ALL EXERCISES
// -------------------------

    public List<Exercise> getAllExercises() {

        return new ArrayList<>(exercises);
    }


// -------------------------
// GET NEXT ID
// -------------------------

    public int getNextId() {

        return nextId++;
    }


// -------------------------
// FIND EXERCISE BY ID
// -------------------------

    public Exercise getExerciseById(int id) {

        for (Exercise exercise : exercises) {

            if (exercise.getId() == id) {
                return exercise;
            }
        }

        return null;
    }


// -------------------------
// REMOVE EXERCISE
// -------------------------

    public void removeExercise(int id) {

        exercises.removeIf(exercise ->
                exercise.getId() == id
        );
    }


// -------------------------
// CLEAR ALL EXERCISES
// -------------------------

    public void clearExercises() {

        exercises.clear();
    }


}