package com.fitapp.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserDatabaseCSVTest {

    @Test
    public void testUserDatabaseCSVClass(){

        UserDatabaseCSV uDCSV = new UserDatabaseCSV();

        // exceptions for validateUser method
        assertThrows(EmptyFieldException.class, ()-> uDCSV.validateInput("User",""));
        assertThrows(EmptyFieldException.class, ()-> uDCSV.validateInput("","pass"));
        assertThrows(InvalidCredentialsException.class, ()-> uDCSV.validateInput("User", "pass"));

    }

}
