package com.fitapp.model;

import com.fitapp.model.Statistic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatisticTest {
    /**
     This class tests the constructor and methods of the Statistic class.
     */
    @Test
    public void testConstructor(){
        Statistic s = new Statistic(
            "2025-05-01_2025-07-01",
                15
        );

        assertEquals("2025-05-01_2025-07-01", s.getPeriod());
        assertEquals(15, s.getNumOfExercises());

    }
}
