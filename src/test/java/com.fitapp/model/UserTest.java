package com.fitapp.model;

import com.fitapp.model.User;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    /**
     This class tests the constructor and methods of the Plan class.
     */
    @Test
    public void testConstructor(){
        User u = new User(
                5,
                "John Doe",
                27,
                75,
                180,
                "male",
                "example@email.com",
                "pass123"
        );

        assertEquals(5, u.getId());
        assertEquals("John Doe", u.getUsername());
        assertEquals(27, u.getAge());
        assertEquals(75, u.getWeight());
        assertEquals(180, u.getHeight());
        assertEquals("male", u.getSex());
        assertEquals("example@email.com", u.getEmail());
        assertEquals("pass123", u.getPassword());
    }

}
