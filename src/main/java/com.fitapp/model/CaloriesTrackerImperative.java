package com.fitapp.model;

public class CaloriesTrackerImperative {

    // attributes
    private int dailyLimit;
    private int consumed;

    // constructor
    public CaloriesTrackerImperative(int dailyLimit) {
        this.dailyLimit = dailyLimit;
        this.consumed = 0;
    }

    // getters and setters
    public int getDailyLimit(){return dailyLimit;}
    public int getConsumed(){return consumed;}

    // acts as a setter for consumed with only one possible value
    public void reset() {
        consumed = 0;
    }

    // methods
    public void addCalories(int calories) {
        if (calories < 0) {
            throw new NegativeCaloriesException();
        }
        consumed += calories;
    }

    public int getRemainingCalories(){
        if((dailyLimit - consumed) < 0){
            throw new CalorieLimitExceededException();
        }
        return dailyLimit - consumed;
    }

}

