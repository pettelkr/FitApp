package com.fitapp.model;

public interface UserRepository {
    /**
     * This interface makes sure that no matter which type of database will be used and implemented that there
     * will be a method for validating the user input and authentication for the user is also handled.
     * At the moment we are using a CSV file which functions as an in-memory database.
     *
     * Why is this useful:
     * It implements the strategy pattern - define a family of behaviors, encapsulate each one and make them interchangebale
     * The caller does not care how something is actually done, he/she just cares what it can do. No matter the underlying
     * database (CSV, SQLite etc.) we need to validate the user input and authenticate the user. The strategies are
     * interchangeable.
     *
     * Furthermore, it supports SOLID principles:
     * Single Responsibility: each strategy does one thing
     * Open/Closed: we can add a new strategy - new way to implement a database - without modifying exisiting code
     * Dependency Inversion: Code details should depend on abstractions, high-level modules(business logic)
     * should not depend on low-level modules(technical details)
     *
     * @param username
     * @param password
     */
    public void validateInput(String username, String password);
    public boolean validateUser(String username, String password);
}
