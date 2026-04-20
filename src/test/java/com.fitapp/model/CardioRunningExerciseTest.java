package com.fitapp.model;

import com.fitapp.model.CardioRunningExercise;

import java.util.Date;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardioRunningExerciseTest {
    /**
     This class tests the constructor and methods of the CardioRunningExercise class.
     */

    @Test
    public void testConstructor(){
        CardioRunningExercise cre = new CardioRunningExercise(
                2,
                "running",
                "running predetermined routes",
                new Date(),
                "4/10",
                60,
                350,
                50.5,
                15,
                4500
        );

        // assertions to test assumptions about our code, in this case the correct construction of a WeighExercise object
        assertEquals(2, cre.getId());
        assertEquals("running", cre.getName());
        assertEquals("running predetermined routes", cre.getDescription());
        //assertEquals(date) needs to figured out;
        assertEquals("4/10", cre.getDifficulty());
        assertEquals(60, cre.getDuration());
        assertEquals(350, cre.getCalories());
        assertEquals(50.5, cre.getDistance());
        assertEquals(15, cre.getSpeed());
        assertEquals(4500, cre.getSteps());
    }
}
