package com.fitapp.model;

import java.util.Date;

public class WeightExercise extends Exercise {

    //attributes
    private double weight;
    private int repetition;
    private String muscleGroup;

    // constructor
    public WeightExercise(int id, String name, String description, Date date, String difficulty, double duration, double calories,
                          double weight, int repetition, String muscleGroup){
        super(id, name, description, date, difficulty, duration,calories);
        this.weight = weight;
        this.repetition = repetition;
        this.muscleGroup = muscleGroup;
    }

    // getters and setters

    public double getWeight(){return weight;}
    public int getRepetition(){return repetition;}
    public String getMuscleGroup(){return muscleGroup;}

    public void setWeight(double weight){

        this.weight = weight;

    }

    @Override
    public double calcCalories(){
        // to be implemented
        return 0;
    }

}
