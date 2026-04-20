package com.fitapp.model;
/* src/domain/User.java
 * Domain-Klasse: User
 * Repräsentiert einen Benutzer (aus Klassendiagramm Bild 1)
 * SOLID: 
 * Single Responsibility - nur Daten und zugehörige Geschäftslogik
 * Open/Closed Principle:
 *   Die Klasse ist so gestaltet, dass sie durch Erweiterungen (z. B. neue Methoden)
 *   erweitert werden kann, ohne bestehende Methoden zu ändern.
 * Liskov Substitution Principle:
 *   Unterklassen müssen sich so verhalten, dass sie überall anstelle der Oberklasse genutzt werden können.
 * Interface Segregation Principle:
 *   Die Klasse ist so gestaltet, dass sie bei Bedarf in kleinere, spezifische Interfaces
 *   aufgeteilt werden kann
 * Dependency Inversion Principle:
 *   Die Klasse hängt nicht direkt von konkreten Implementierungen ab (z.B. Datenbank),
 *   sondern könnte z.B. über Interfaces oder Services mit externen Komponenten arbeiten.
 */

import java.util.List;

public class User implements Exportable{
    // attributes
    private int id;
    private String username;
    private int age;
    private double weight;
    private double height;
    private String sex;
    private String email;
    private String password;



    // constructor
    public User(int id, String username, int age, double weight, double height, String sex, String email, String password) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.sex = sex;
        this.email = email;
        this.password = password;
    }
  
    // methods
    public User register(int id, String username, int age, double weight, double height, String sex, String email, String password){
        return new User(id, username, age, weight, height, sex, email, password);
    }

    public boolean signIn(String email, String password){
        // to be implemented
        return true;
    }

    public void delete(List<User> users) {

        users.remove(this);
    }
  
    // method implementations of Exportable interface
    public String exportAsPDF(){
        return "Implement method";
    }

    public String exportAsJSON(){
        return "Implement method";
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public String getSex(){
        return sex;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword() {
        return password;
    }


}
