package com.fitapp.model;

import com.fitapp.model.CaloriesTracker;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CaloriesTrackerTest {

    @Test
    public void testCaloriesTrackerClass(){
        CaloriesTracker calTrack = new CaloriesTracker(
                2000
        );

        // assertions to test assumptions about our code, in this case the correct construction of a WeighExercise object
        assertEquals(2000, calTrack.getDailyLimit().get());

        // happy path
        calTrack.addCalories(42);
        assertEquals(42, calTrack.getConsumed().get());
        calTrack.addCalories(58);
        assertEquals(100, calTrack.getConsumed().get());

        // exceptions
        assertThrows(NegativeCaloriesException.class, ()-> calTrack.addCalories(-25));
        assertThrows(CalorieLimitExceededException.class, ()-> calTrack.addCalories(2500));
    }
}
