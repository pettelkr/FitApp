package com.fitapp.model;

import java.util.Date;
import java.util.List;

public class Plan {
    // attributes
    private int id;
    private String name;
    private Date startDate;
    private Date endDate;
    private List<Exercise> exercises;

    // constructor
    public Plan(int id, String name, Date startDate, Date endDate, List<Exercise> exercises){
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.exercises = exercises;
    }

    // getters and setters

    public int getId(){return id;}
    public String getName(){return name;}
    public Date getStartDate(){return startDate;}
    public Date getEndDate(){return endDate;}
    public List<Exercise> getExercises(){return exercises;}

    // methods

    public void addExercise(Exercise ex){
        this.exercises.add(ex);
    }

    public double getTotalCalories(){
        // to be implemented
        return 0;
    }
}
