package com.fitapp.controller;

import com.fitapp.model.*;
import com.fitapp.navigation.Navigator;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.time.LocalDate;

public class CaloricIntakeController implements Controller {

    private Navigator navigator;
    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void changeView(String fxmlFile) {
        navigator.changeView(fxmlFile);
    }

    // -------------------------
    // MODEL
    // -------------------------
    private CaloriesTracker calTra;

    // Zugriff auf die Datenbank, damit die Werte einen Neustart ueberleben.
    private final CalorieRepository calorieDB = new CalorieDatabase();
    //heute verbrannte Kalorien, gemerkt fuer den Fall eines neuen Ziels.
    private int burnedToday;

    // -------------------------
    // FXML FIELDS
    // -------------------------
    @FXML
    private TextField goalField;

    @FXML
    private TextField caloriesField;

    @FXML
    private TextField remainingField;

    @FXML
    private Label caloriesOverflowLabel;

    public CaloricIntakeController(){}

    /**
     * Wird vom FXMLLoader aufgerufen. Laedt Ziel und die heute schon
     * gegessenen Kalorien aus der Datenbank und stellt den Tracker
     * damit wieder her.
     */
    @FXML
    public void initialize() {
        caloriesOverflowLabel.setVisible(false);

        if (!Session.isLoggedIn()) {
            showMessage("No user logged in.");
            return;
        }

        int userId = Session.getUserId();
        LocalDate today = LocalDate.now();

        Task<int[]> task = new Task<>() {
            @Override
            protected int[] call() throws Exception {
                return new int[]{
                        calorieDB.getGoal(userId),
                        calorieDB.getEatenToday(userId, today),
                        calorieDB.getBurnedToday(userId, today)
                };
            }
        };

        task.setOnSucceeded(event -> {
            int[] values = task.getValue();
            restoreTracker(values[0], values[1], values[2]);
        });
        task.setOnFailed(event -> showMessage("Could not load saved data."));

        runInBackground(task);
    }
    /**
     * Baut den Tracker mit dem gespeicherten Ziel auf und spielt die
     * bereits gegessenen Kalorien nach.
     */
    private void restoreTracker(int goal, int eaten, int burned) {
        burnedToday = burned;

        calTra = new CaloriesTracker(goal);
        calTra.setBurned(burned);
        bindRemaining();

        goalField.setPromptText("Current goal: " + goal);

        if (eaten <= 0) {
            return;
        }
        try {
            calTra.addCalories(eaten);
        } catch (NegativeCaloriesException | CalorieLimitExceededException e) {
            showMessage("Exceeded daily calorie limit!");
        }
    }

    // -------------------------
    // SET GOAL (USER INPUT)
    // -------------------------
    @FXML
    public void handleSetGoal(ActionEvent event) {
        try {
            int goal = Integer.parseInt(goalField.getText());

            if (calTra == null) {
                calTra = new CaloriesTracker(goal);
                calTra.setBurned(burnedToday);
                bindRemaining();
            } else {
                //nur das Ziel aendern. Die gegessenen Kalorien stehen in
                // der Datenbank und wuerden bei einem neuen Tracker verschwinden.
                // Die Binding rechnet automatisch neu.
                calTra.getDailyLimit().set(goal);
            }

            caloriesOverflowLabel.setVisible(false);
            // Ziel speichern
            persist(() -> calorieDB.setGoal(Session.getUserId(), goal),
                    "Could not save the goal.");

        } catch (NumberFormatException e) {
            showMessage("Please enter a valid calorie goal.");

        }
    }

    // -------------------------
    // ADD CALORIES
    // -------------------------
    @FXML
    public void handleAddingCalories() {

        if (calTra == null) {
            caloriesOverflowLabel.setText("Please set a calorie goal first.");
            caloriesOverflowLabel.setVisible(true);
            return;
        }

        try {
            int calories = Integer.parseInt(caloriesField.getText());

            calTra.addCalories(calories);

            remainingField.textProperty().bind(
                    calTra.remainingCaloriesProperty().asString()
            );

            caloriesOverflowLabel.setVisible(false);

            //Mahlzeit speichern. meals.name ist NOT NULL, das Formular
            // fragt aber keinen Namen ab, deshalb ein fester Wert.
            persist(() -> calorieDB.addMeal(Session.getUserId(), "Meal",
                            calories, LocalDate.now()),
                    "Could not save the entry.");

        } catch (NegativeCaloriesException e) {
            caloriesOverflowLabel.setText("Calories must be a positive number!");
            caloriesOverflowLabel.setVisible(true);

        } catch (CalorieLimitExceededException e) {
            caloriesOverflowLabel.setText("Exceeded daily calorie limit!");
            caloriesOverflowLabel.setVisible(true);

        } catch (NumberFormatException e) {
            caloriesOverflowLabel.setText("Please enter a valid number.");
            caloriesOverflowLabel.setVisible(true);
        }
    }

    // -------------------------
    // RESET
    // -------------------------
    @FXML
    public void handleReset(ActionEvent event) {

        if (calTra != null) {
            calTra.reset();
        }

        caloriesField.setText("");
        caloriesOverflowLabel.setVisible(false);

        //auch in der Datenbank loeschen, sonst kommen die Werte
        // beim naechsten Oeffnen der Ansicht zurueck.
        persist(() -> calorieDB.resetMeals(Session.getUserId(), LocalDate.now()),
                "Could not reset the entries.");
    }

    // -------------------------
    // BACK
    // -------------------------
    @FXML
    public void handleBackToMenu(ActionEvent event) {
        changeView("mainMenu.fxml");
    }

    //Hilfsmethoden
    private void bindRemaining() {
        remainingField.textProperty().bind(
                calTra.remainingCaloriesProperty().asString()
        );
    }

    /**
     * Schreibt im Hintergrund in die Datenbank, damit die Oberflaeche
     * nicht einfriert. Der Tracker wurde vorher schon aktualisiert,
     * die Anzeige stimmt also sofort.
     */
    private void persist(DatabaseAction action, String errorMessage) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                action.run();
                return null;
            }
        };

        task.setOnFailed(event -> showMessage(errorMessage));
        runInBackground(task);
    }

    /** Daemon-Thread, damit ein haengender Zugriff das Schliessen nicht blockiert. */
    private void runInBackground(Task<?> task) {
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void showMessage(String message) {
        caloriesOverflowLabel.setText(message);
        caloriesOverflowLabel.setVisible(true);
    }

    /** Alles, was eine Exception werfen darf und nichts zurueckgibt. */
    @FunctionalInterface
    private interface DatabaseAction {
        void run() throws Exception;
    }
}