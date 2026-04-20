package com.fitapp.model;

import com.fitapp.model.CardioCalisthenicsExercise;

import java.util.Date;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class CardioCalisthenicsExerciseTest {

    @Test
    public void testCardioCalisthenicClass() {
        CardioCalisthenicsExercise cce = new CardioCalisthenicsExercise(
                3,
                "Core Workout",
                "Training the core with various exercises",
                new Date(),
                "6/10",
                30,
                400,
                5,
                6,
                5

        );


        // assertions to test assumptions about our code, in this case the correct construction of a WeighExercise object
        assertEquals(3, cce.getId());
        assertEquals("Core Workout", cce.getName());
        assertEquals("Training the core with various exercises", cce.getDescription());
        //assertEquals(date) needs to figured out;
        assertEquals("6/10", cce.getDifficulty());
        assertEquals(30, cce.getDuration());
        assertEquals(400, cce.getCalories());
        assertEquals(5, cce.getInterval());
        assertEquals(6, cce.getNumOfExercisesPerRound());
        assertEquals(5, cce.getRound());

        int result = cce.getTotalNumOfEx(3,5);
        assertEquals(15, result);


    }
}
