package com.fitapp.model;

public class Statistic {
    // attributes
    private String period;
    private int numOfExercises;

    //constructor
    public Statistic(String period, int numOfExercises){
        this.period = period;
        this.numOfExercises = numOfExercises;
    }

    // getters and setters
    public String getPeriod(){return period;}
    public int getNumOfExercises(){return numOfExercises;}

    //methods
    public void calcStatistic(){
        // to be implemented
    }

    public double getAverage(){
        // to be implemented
        return 0;
    }

    public String export(){
        // to be implemented
        return "Implement this method.";
    }

}
