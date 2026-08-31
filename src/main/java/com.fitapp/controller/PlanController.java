package com.fitapp.controller;

import com.fitapp.model.*;
import com.fitapp.navigation.Navigator;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class PlanController implements Controller {

    // =====================================================
    // NAVIGATION
    // =====================================================

    private Navigator navigator;

    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void changeView(String fxmlFile) {
        navigator.changeView(fxmlFile);
    }


    // =====================================================
    // SERVICES / MODEL
    // =====================================================

    private final PlanService planService = new PlanService();

    /*
     * ExerciseService verwendet eine statische Liste.
     *
     * Dadurch greifen ExerciseController und
     * PlanController auf dieselben Übungen zu.
     */
    private final ExerciseService exerciseService =
            new ExerciseService();

    private Plan currentPlan;


    // =====================================================
    // FXML - PLAN
    // =====================================================

    @FXML
    private TextField planNameField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;


    // =====================================================
    // FXML - DAYS
    // =====================================================

    @FXML
    private ComboBox<String> dayField;

    @FXML
    private ComboBox<PlanDay> daySelector;


    // =====================================================
    // FXML - EXERCISES
    // =====================================================

    @FXML
    private ComboBox<String> exerciseTypeBox;

    @FXML
    private ComboBox<Exercise> exerciseBox;


    // =====================================================
    // FXML - PLAN EXERCISE DATA
    // =====================================================

    @FXML
    private TextField durationField;

    @FXML
    private TextField setsField;

    @FXML
    private TextField repsField;


    // =====================================================
    // FXML - PLAN OVERVIEW
    // =====================================================

    @FXML
    private Label overviewPlanName;

    @FXML
    private Label overviewDates;

    @FXML
    private VBox overviewContainer;

    @FXML
    private Label overviewDuration;

    @FXML
    private Label overviewCalories;


    // =====================================================
    // INITIALIZE
    // =====================================================

    @FXML
    public void initialize() {

        // ---------------------------------------------
        // Exercise categories
        // ---------------------------------------------

        exerciseTypeBox.setItems(
                FXCollections.observableArrayList(
                        "WEIGHT",
                        "CARDIO_RUNNING",
                        "CARDIO_CALISTHENICS"
                )
        );


        // ---------------------------------------------
        // Weekdays
        // ---------------------------------------------

        dayField.setItems(
                FXCollections.observableArrayList(
                        "Montag",
                        "Dienstag",
                        "Mittwoch",
                        "Donnerstag",
                        "Freitag",
                        "Samstag",
                        "Sonntag"
                )
        );


        // ---------------------------------------------
        // Initially no exercises
        // ---------------------------------------------

        exerciseBox.setItems(
                FXCollections.observableArrayList()
        );


        // ---------------------------------------------
        // Initially empty overview
        // ---------------------------------------------

        updateOverview();


        // ---------------------------------------------
        // React to exercise type selection
        // ---------------------------------------------

        exerciseTypeBox.setOnAction(
                event -> handleTypeSelect()
        );
    }


    // =====================================================
    // CREATE PLAN
    // =====================================================

    @FXML
    public void handleCreatePlan() {

        String planName =
                planNameField.getText().trim();


        // ---------------------------------------------
        // Validation
        // ---------------------------------------------

        if (planName.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Missing information",
                    "Please enter a plan name."
            );

            return;
        }


        if (startDatePicker.getValue() == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Missing information",
                    "Please select a start date."
            );

            return;
        }


        if (endDatePicker.getValue() == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Missing information",
                    "Please select an end date."
            );

            return;
        }


        if (endDatePicker.getValue()
                .isBefore(startDatePicker.getValue())) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Invalid dates",
                    "The end date cannot be before the start date."
            );

            return;
        }


        // ---------------------------------------------
        // Create plan
        // ---------------------------------------------

        currentPlan = planService.createPlan(
                1,
                planName,
                java.sql.Date.valueOf(
                        startDatePicker.getValue()
                ),
                java.sql.Date.valueOf(
                        endDatePicker.getValue()
                )
        );


        // ---------------------------------------------
        // Reset day selector
        // ---------------------------------------------

        daySelector.setItems(
                FXCollections.observableArrayList()
        );


        // ---------------------------------------------
        // Reset exercise selector
        // ---------------------------------------------

        exerciseBox.setItems(
                FXCollections.observableArrayList()
        );


        // ---------------------------------------------
        // Update overview
        // ---------------------------------------------

        updateOverview();


        showAlert(
                Alert.AlertType.INFORMATION,
                "Plan created",
                "Training plan \"" +
                        planName +
                        "\" was created successfully."
        );
    }


    // =====================================================
    // ADD DAY
    // =====================================================

    @FXML
    public void handleAddDay() {

        // ---------------------------------------------
        // Check plan
        // ---------------------------------------------

        if (currentPlan == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "No plan",
                    "Create a training plan first."
            );

            return;
        }


        // ---------------------------------------------
        // Get day
        // ---------------------------------------------

        String dayName =
                dayField.getValue();


        if (dayName == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Missing day",
                    "Please select a day."
            );

            return;
        }


        // ---------------------------------------------
        // Check duplicate
        // ---------------------------------------------

        if (currentPlan.getDay(dayName) != null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Day already exists",
                    dayName + " has already been added."
            );

            return;
        }


        // ---------------------------------------------
        // Add day
        // ---------------------------------------------

        planService.addDayToPlan(
                currentPlan,
                dayName
        );


        refreshDays();


        // ---------------------------------------------
        // Select newly added day
        // ---------------------------------------------

        for (PlanDay day : daySelector.getItems()) {

            if (day.getDayName()
                    .equalsIgnoreCase(dayName)) {

                daySelector.setValue(day);
                break;
            }
        }


        // ---------------------------------------------
        // Update overview
        // ---------------------------------------------

        updateOverview();


        showAlert(
                Alert.AlertType.INFORMATION,
                "Day added",
                "Day \"" +
                        dayName +
                        "\" was added to the plan."
        );
    }


    // =====================================================
    // REFRESH DAYS
    // =====================================================

    private void refreshDays() {

        if (currentPlan == null) {
            return;
        }

        daySelector.setItems(
                FXCollections.observableArrayList(
                        currentPlan.getDays()
                )
        );
    }


    // =====================================================
    // SELECT EXERCISE TYPE
    // =====================================================

    @FXML
    public void handleTypeSelect() {

        String selectedType =
                exerciseTypeBox.getValue();


        // ---------------------------------------------
        // No type selected
        // ---------------------------------------------

        if (selectedType == null) {

            exerciseBox.setItems(
                    FXCollections.observableArrayList()
            );

            return;
        }


        // ---------------------------------------------
        // Get all created exercises
        // ---------------------------------------------

        List<Exercise> allExercises =
                exerciseService.getAllExercises();


        List<Exercise> filteredExercises =
                new ArrayList<>();


        // ---------------------------------------------
        // Filter exercises by type
        // ---------------------------------------------

        for (Exercise exercise : allExercises) {

            if ("WEIGHT".equals(selectedType)
                    && exercise instanceof WeightExercise) {

                filteredExercises.add(exercise);
            }

            else if (
                    "CARDIO_RUNNING".equals(selectedType)
                            && exercise instanceof CardioRunningExercise) {

                filteredExercises.add(exercise);
            }

            else if (
                    "CARDIO_CALISTHENICS".equals(selectedType)
                            && exercise instanceof CardioCalisthenicsExercise) {

                filteredExercises.add(exercise);
            }
        }


        // ---------------------------------------------
        // Put exercises into ComboBox
        // ---------------------------------------------

        exerciseBox.setItems(
                FXCollections.observableArrayList(
                        filteredExercises
                )
        );


        // ---------------------------------------------
        // Reset previous selection
        // ---------------------------------------------

        exerciseBox.getSelectionModel()
                .clearSelection();


        // ---------------------------------------------
        // Console information
        // ---------------------------------------------

        if (filteredExercises.isEmpty()) {

            System.out.println(
                    "No exercises found for type: "
                            + selectedType
            );
        }
    }


    // =====================================================
    // ADD EXERCISE TO PLAN
    // =====================================================

    @FXML
    public void handleAddExercise() {

        // ---------------------------------------------
        // Check plan
        // ---------------------------------------------

        if (currentPlan == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "No plan",
                    "Create a training plan first."
            );

            return;
        }


        // ---------------------------------------------
        // Selected day
        // ---------------------------------------------

        PlanDay selectedDay =
                daySelector.getValue();


        if (selectedDay == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Missing day",
                    "Please select a day."
            );

            return;
        }


        // ---------------------------------------------
        // Selected exercise
        // ---------------------------------------------

        Exercise selectedExercise =
                exerciseBox.getValue();


        if (selectedExercise == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Missing exercise",
                    "Please select an exercise."
            );

            return;
        }


        // =================================================
        // DURATION
        // =================================================

        double duration;

        String durationText =
                durationField.getText().trim();


        if (durationText.isEmpty()) {

            /*
             * Wenn keine eigene Dauer angegeben wird,
             * wird die Dauer der Übung verwendet.
             */

            duration =
                    selectedExercise.getDuration();

        } else {

            try {

                duration =
                        Double.parseDouble(durationText);

            } catch (NumberFormatException e) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Invalid duration",
                        "Duration must be a number."
                );

                return;
            }
        }


        if (duration <= 0) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Invalid duration",
                    "Duration must be greater than 0."
            );

            return;
        }


        // =================================================
        // SETS
        // =================================================

        int sets = 0;

        String setsText =
                setsField.getText().trim();


        if (!setsText.isEmpty()) {

            try {

                sets =
                        Integer.parseInt(setsText);

            } catch (NumberFormatException e) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Invalid sets",
                        "Sets must be a whole number."
                );

                return;
            }


            if (sets < 0) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Invalid sets",
                        "Sets cannot be negative."
                );

                return;
            }
        }


        // =================================================
        // REPS
        // =================================================

        int reps = 0;

        String repsText =
                repsField.getText().trim();


        if (!repsText.isEmpty()) {

            try {

                reps =
                        Integer.parseInt(repsText);

            } catch (NumberFormatException e) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Invalid reps",
                        "Reps must be a whole number."
                );

                return;
            }


            if (reps < 0) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Invalid reps",
                        "Reps cannot be negative."
                );

                return;
            }
        }


        // =================================================
        // ADD EXERCISE TO PLAN
        // =================================================

        planService.addExerciseToDay(
                currentPlan,
                selectedDay.getDayName(),
                selectedExercise,
                duration,
                sets,
                reps
        );


        // =================================================
        // CALCULATE CALORIES
        // =================================================

        double calories =
                selectedExercise.getCalories()
                        * duration
                        / 60.0;


        // =================================================
        // CONSOLE OUTPUT
        // =================================================

        System.out.println(
                "Exercise added:"
        );

        System.out.println(
                "Name: "
                        + selectedExercise.getName()
        );

        System.out.println(
                "Day: "
                        + selectedDay.getDayName()
        );

        System.out.println(
                "Duration: "
                        + duration
                        + " min"
        );

        System.out.println(
                "Sets: "
                        + sets
        );

        System.out.println(
                "Reps: "
                        + reps
        );

        System.out.println(
                "Calories: "
                        + calories
        );


        // =================================================
        // UPDATE OVERVIEW
        // =================================================

        updateOverview();


        // =================================================
        // RESET INPUT FIELDS
        // =================================================

        durationField.clear();
        setsField.clear();
        repsField.clear();


        // =================================================
        // SHOW RESULT
        // =================================================

        showAlert(
                Alert.AlertType.INFORMATION,
                "Exercise added",
                selectedExercise.getName()
                        + " was added to "
                        + selectedDay.getDayName()
                        + ".\n\n"
                        + "Duration: "
                        + duration
                        + " min\n"
                        + "Sets: "
                        + (sets > 0 ? sets : "-")
                        + "\n"
                        + "Reps: "
                        + (reps > 0 ? reps : "-")
                        + "\n"
                        + "Calories: "
                        + String.format(
                        "%.1f",
                        calories
                )
                        + " kcal"
        );
    }


    // =====================================================
    // UPDATE PLAN OVERVIEW
    // =====================================================

    private void updateOverview() {

        // ---------------------------------------------
        // Check FXML
        // ---------------------------------------------

        if (overviewPlanName == null
                || overviewDates == null
                || overviewContainer == null
                || overviewDuration == null
                || overviewCalories == null) {

            return;
        }


        // ---------------------------------------------
        // No plan
        // ---------------------------------------------

        if (currentPlan == null) {

            overviewPlanName.setText(
                    "No plan created"
            );

            overviewDates.setText(
                    "Start: - | End: -"
            );

            overviewContainer.getChildren()
                    .clear();

            overviewDuration.setText(
                    "Total duration: 0 min"
            );

            overviewCalories.setText(
                    "Total calories: 0 kcal"
            );

            return;
        }


        // ---------------------------------------------
        // Plan information
        // ---------------------------------------------

        overviewPlanName.setText(
                currentPlan.getName()
        );


        overviewDates.setText(
                "Start: "
                        + currentPlan.getStartDate()
                        + " | End: "
                        + currentPlan.getEndDate()
        );


        // ---------------------------------------------
        // Clear old overview
        // ---------------------------------------------

        overviewContainer.getChildren()
                .clear();


        // ---------------------------------------------
        // Add days
        // ---------------------------------------------

        for (PlanDay day :
                currentPlan.getDays()) {

            VBox dayBox =
                    new VBox(5);

            dayBox.setStyle(
                    "-fx-background-color: rgba(240,240,240,0.9);"
                            + "-fx-background-radius: 8;"
                            + "-fx-padding: 10;"
            );


            // -----------------------------------------
            // Day name
            // -----------------------------------------

            Label dayLabel =
                    new Label(
                            day.getDayName()
                    );

            dayLabel.setStyle(
                    "-fx-font-weight: bold;"
                            + "-fx-font-size: 15px;"
            );


            dayBox.getChildren()
                    .add(dayLabel);


            // -----------------------------------------
            // No exercises
            // -----------------------------------------

            if (day.isEmpty()) {

                Label emptyLabel =
                        new Label(
                                "No exercises"
                        );

                emptyLabel.setStyle(
                        "-fx-text-fill: gray;"
                );

                dayBox.getChildren()
                        .add(emptyLabel);
            }


            // -----------------------------------------
            // Exercises
            // -----------------------------------------

            else {

                for (PlanExercise planExercise :
                        day.getExercises()) {

                    Exercise exercise =
                            planExercise.getExercise();


                    VBox exerciseBox =
                            new VBox(2);


                    exerciseBox.setStyle(
                            "-fx-padding: 5 0 5 10;"
                    );


                    // Exercise name

                    Label nameLabel =
                            new Label(
                                    exercise.getName()
                            );

                    nameLabel.setStyle(
                            "-fx-font-weight: bold;"
                    );


                    exerciseBox.getChildren()
                            .add(nameLabel);


                    // Duration

                    Label durationLabel =
                            new Label(
                                    "Duration: "
                                            + String.format(
                                            "%.1f",
                                            planExercise.getDuration()
                                    )
                                            + " min"
                            );

                    exerciseBox.getChildren()
                            .add(durationLabel);


                    // Sets

                    if (planExercise.getSets() > 0) {

                        Label setsLabel =
                                new Label(
                                        "Sets: "
                                                + planExercise.getSets()
                                );

                        exerciseBox.getChildren()
                                .add(setsLabel);
                    }


                    // Reps

                    if (planExercise.getReps() > 0) {

                        Label repsLabel =
                                new Label(
                                        "Reps: "
                                                + planExercise.getReps()
                                );

                        exerciseBox.getChildren()
                                .add(repsLabel);
                    }


                    // Calories

                    Label caloriesLabel =
                            new Label(
                                    "Calories: "
                                            + String.format(
                                            "%.1f",
                                            planExercise.getCalories()
                                    )
                                            + " kcal"
                            );

                    exerciseBox.getChildren()
                            .add(caloriesLabel);


                    dayBox.getChildren()
                            .add(exerciseBox);
                }
            }


            // -----------------------------------------
            // Day totals
            // -----------------------------------------

            if (!day.isEmpty()) {

                Label dayTotalLabel =
                        new Label(
                                "Day total: "
                                        + String.format(
                                        "%.1f",
                                        day.getTotalDuration()
                                )
                                        + " min | "
                                        + String.format(
                                        "%.1f",
                                        day.getTotalCalories()
                                )
                                        + " kcal"
                        );


                dayTotalLabel.setStyle(
                        "-fx-font-weight: bold;"
                                + "-fx-padding: 5 0 0 0;"
                );


                dayBox.getChildren()
                        .add(dayTotalLabel);
            }


            // -----------------------------------------
            // Add day to overview
            // -----------------------------------------

            overviewContainer.getChildren()
                    .add(dayBox);
        }


        // ---------------------------------------------
        // Plan totals
        // ---------------------------------------------

        overviewDuration.setText(
                "Total duration: "
                        + String.format(
                        "%.1f",
                        currentPlan.getTotalDuration()
                )
                        + " min"
        );


        overviewCalories.setText(
                "Total calories: "
                        + String.format(
                        "%.1f",
                        currentPlan.getTotalCalories()
                )
                        + " kcal"
        );
    }


    // =====================================================
    // TEXT OVERVIEW
    // =====================================================

    public String getPlanOverview() {

        if (currentPlan == null) {

            return "No training plan created.";
        }


        StringBuilder overview =
                new StringBuilder();


        overview.append(
                "TRAINING PLAN\n"
        );

        overview.append(
                "==============================\n"
        );

        overview.append(
                "Name: "
                        + currentPlan.getName()
                        + "\n"
        );

        overview.append(
                "Start: "
                        + currentPlan.getStartDate()
                        + "\n"
        );

        overview.append(
                "End: "
                        + currentPlan.getEndDate()
                        + "\n\n"
        );


        // ---------------------------------------------
        // Days
        // ---------------------------------------------

        for (PlanDay day :
                currentPlan.getDays()) {

            overview.append(
                    day.getDayName()
            );

            overview.append(
                    "\n------------------------------\n"
            );


            if (day.isEmpty()) {

                overview.append(
                        "No exercises\n\n"
                );

                continue;
            }


            // -----------------------------------------
            // Exercises
            // -----------------------------------------

            for (PlanExercise planExercise :
                    day.getExercises()) {

                Exercise exercise =
                        planExercise.getExercise();


                overview.append(
                        "Exercise: "
                                + exercise.getName()
                                + "\n"
                );


                overview.append(
                        "Duration: "
                                + planExercise.getDuration()
                                + " min\n"
                );


                if (planExercise.getSets() > 0) {

                    overview.append(
                            "Sets: "
                                    + planExercise.getSets()
                                    + "\n"
                    );
                }


                if (planExercise.getReps() > 0) {

                    overview.append(
                            "Reps: "
                                    + planExercise.getReps()
                                    + "\n"
                    );
                }


                overview.append(
                        "Calories: "
                                + String.format(
                                "%.1f",
                                planExercise.getCalories()
                        )
                                + " kcal\n\n"
                );
            }


            // -----------------------------------------
            // Day totals
            // -----------------------------------------

            overview.append(
                    "Day total duration: "
                            + String.format(
                            "%.1f",
                            day.getTotalDuration()
                    )
                            + " min\n"
            );


            overview.append(
                    "Day total calories: "
                            + String.format(
                            "%.1f",
                            day.getTotalCalories()
                    )
                            + " kcal\n\n"
            );
        }


        // ---------------------------------------------
        // Plan totals
        // ---------------------------------------------

        overview.append(
                "==============================\n"
        );

        overview.append(
                "TOTAL PLAN DURATION: "
                        + String.format(
                        "%.1f",
                        currentPlan.getTotalDuration()
                )
                        + " min\n"
        );

        overview.append(
                "TOTAL PLAN CALORIES: "
                        + String.format(
                        "%.1f",
                        currentPlan.getTotalCalories()
                )
                        + " kcal\n"
        );


        return overview.toString();
    }


    // =====================================================
    // SHOW PLAN OVERVIEW
    // =====================================================

    @FXML
    public void handleShowPlanOverview() {

        if (currentPlan == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "No plan",
                    "Create a training plan first."
            );

            return;
        }


        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );


        alert.setTitle(
                "Training Plan Overview"
        );

        alert.setHeaderText(
                currentPlan.getName()
        );

        alert.setContentText(
                getPlanOverview()
        );


        alert.getDialogPane()
                .setPrefWidth(500);

        alert.getDialogPane()
                .setPrefHeight(600);

        alert.showAndWait();
    }


    // =====================================================
    // BACK TO MENU
    // =====================================================

    @FXML
    public void handleBackToMenu() {

        changeView(
                "mainMenu.fxml"
        );
    }


    // =====================================================
    // ALERT
    // =====================================================

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert =
                new Alert(type);

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }
}