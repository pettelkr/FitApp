package com.fitapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Plan {

    // -------------------------
    // ATTRIBUTES
    // -------------------------

    private int id;
    private String name;
    private Date startDate;
    private Date endDate;

    private List<PlanDay> days;


    // -------------------------
    // CONSTRUCTOR
    // -------------------------

    public Plan(
            int id,
            String name,
            Date startDate,
            Date endDate,
            List<PlanDay> days) {

        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;

        this.days = (days != null)
                ? days
                : new ArrayList<>();
    }


    // -------------------------
    // GETTERS
    // -------------------------

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public List<PlanDay> getDays() {
        return days;
    }


    // -------------------------
    // DAY MANAGEMENT
    // -------------------------

    public void addDay(PlanDay day) {

        if (day != null) {
            days.add(day);
        }
    }


    public PlanDay getDay(String dayName) {

        for (PlanDay day : days) {

            if (day.getDayName().equalsIgnoreCase(dayName)) {
                return day;
            }
        }

        return null;
    }


    public void removeDay(String dayName) {

        days.removeIf(
                day ->
                        day.getDayName()
                                .equalsIgnoreCase(dayName)
        );
    }


    // -------------------------
    // CALCULATIONS
    // -------------------------

    public double getTotalCalories() {

        double total = 0;

        for (PlanDay day : days) {

            total += day.getTotalCalories();
        }

        return total;
    }


    public double getTotalDuration() {

        double total = 0;

        for (PlanDay day : days) {

            total += day.getTotalDuration();
        }

        return total;
    }


    // -------------------------
    // NUMBER OF DAYS
    // -------------------------

    public int getNumberOfDays() {

        return days.size();
    }
}
