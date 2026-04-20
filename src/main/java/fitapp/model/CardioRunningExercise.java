package com.fitapp.model;

import java.util.Date;

public class CardioRunningExercise extends Exercise{
    //attributes
    private double distance;
    private double speed;
    private int steps;

    // constructor
    public CardioRunningExercise(int id, String name, String description, Date date, String difficulty, double duration, double calories,
                                 double distance, double speed, int steps ){
        super(id, name, description, date, difficulty, duration,calories);
        this.distance = distance;
        this.speed = speed;
        this.steps = steps;

    }

    // getters and setters

    public double getDistance(){return distance;}
    public double getSpeed(){return speed;}
    public int getSteps(){return steps;}

    // methods

    @Override
    public double calcCalories(){
        // to be implemented
        return 0;
    }

}
