package com.fitapp.model;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.opencsv.CSVReader;

public class UserDatabaseCSV implements UserRepository{

    public void validateInput(String username, String password){
        if (username == null || username.isBlank()) {
            throw new EmptyFieldException("Username");
        }

        if (password == null || password.isBlank()) {
            throw new EmptyFieldException("Password");
        }

        if (!validateUser(username, password)) {
            // never be specific, never reveal which part was wrong, just a generic message
            throw new InvalidCredentialsException();
        }
    }
    public boolean validateUser(String username, String password) {
        try (InputStream is = getClass().getResourceAsStream("/data/users.csv");
             CSVReader reader = new CSVReader(new InputStreamReader(is))) {

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine[0].equals(username) && nextLine[1].equals(password)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}

