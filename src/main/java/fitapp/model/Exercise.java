package com.fitapp.model;

import java.util.Date;

public abstract class Exercise {
    // attributes
    private int id;
    private String name;
    private String description;
    private Date date;
    private String difficulty;
    private double duration;
    private double calories;

    // constructor

    public Exercise(int id, String name, String description, Date date, String difficulty, double duration, double calories){

        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.difficulty = difficulty;
        this.duration = duration;
        this.calories = calories;
    }

    // getters and setters

    public int getId(){return id;}
    public String getName(){return name;}
    public String getDescription(){return description;}
    public Date getDate(){return date;}
    public String getDifficulty(){return difficulty;}
    public double getDuration(){return duration;}
    public double getCalories(){return calories;}


    // methods

    public void save(){
        // to be implemented
    }

    public double calcCalories(){
        // to be implemented
        return 0;
    }



}
