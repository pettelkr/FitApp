package com.fitapp.model;

import com.fitapp.model.WeightExercise;

import java.util.Date;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeightExerciseTest {
    /**
     This class tests the constructor and methods of the WeightExercise class.
    */
    @Test
    public void testConstructor(){
        WeightExercise w = new WeightExercise(
                1,
                "butterfly",
                "trains primarily the chest area",
                new Date(),
                "3/10",
                5.5,
                125,
                40,
                3,
                "chest");

        // assertions to test assumptions about our code, in this case the correct construction of a WeighExercise object
        assertEquals(1, w.getId());
        assertEquals("butterfly", w.getName());
        assertEquals("trains primarily the chest area", w.getDescription());
        //assertEquals(date) needs to figured out;
        assertEquals("3/10", w.getDifficulty());
        assertEquals(5.5, w.getDuration());
        assertEquals(125, w.getCalories());
        assertEquals(40, w.getWeight());
        assertEquals(3, w.getRepetition());
        assertEquals("chest", w.getMuscleGroup());

    }

}
