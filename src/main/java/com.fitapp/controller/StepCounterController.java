package com.fitapp.controller;

import com.fitapp.model.*;
import com.fitapp.navigation.Navigator;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.fitapp.util.BackgroundImageHelper;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.time.LocalDate;

public class StepCounterController implements Controller {

    private Navigator navigator;

    private static final int DEFAULT_STEP_GOAL = 10000;
    // true, solange initialize() noch aus der Datenbank liest.
    private boolean loading = true;

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
    private StepTracker stepTracker;

    private final StepRepository stepDB = new StepDatabase();

    // -------------------------
    // FXML FIELDS
    // -------------------------
    @FXML private StackPane rootPane;
    @FXML private ImageView backgroundImage;
    @FXML private TextField goalField;
    @FXML private TextField stepsField;
    @FXML private TextField remainingField;
    @FXML private Label stepOverflowLabel;

    /** Lädt Ziel und die heute schon gelaufenen Schritte aus der Datenbank. */
    @FXML
    public void initialize() {

        BackgroundImageHelper.setup(
                rootPane,
                backgroundImage
        );

        stepOverflowLabel.setVisible(false);

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
                        stepDB.getGoal(userId),
                        stepDB.getStepsToday(userId, today)
                };
            }
        };

        task.setOnSucceeded(event -> {
            int[] values = task.getValue();
            restoreTracker(values[0], values[1]);
            loading = false;
        });
        task.setOnFailed(event -> {
            showMessage("Could not load saved data.");
            loading = false;
        });
        runInBackground(task);
    }

    private void restoreTracker(int goal, int steps) {
        stepTracker = new StepTracker(goal > 0 ? goal : DEFAULT_STEP_GOAL);
        stepTracker.setSteps(steps);
        bindRemaining();

        goalField.setPromptText("Current goal: " + stepTracker.getDailyGoal().get());
    }

    // -------------------------
    // SET GOAL
    // -------------------------
    @FXML
    public void handleSetGoal(ActionEvent event) {
        if (loading) {
            showMessage("Loading, please wait...");
            return;
        }
        try {
            int goal = Integer.parseInt(goalField.getText());

            if (stepTracker == null) {
                stepTracker = new StepTracker(goal);
                bindRemaining();
            } else {
                // nur das Ziel ändern. Ein neuer Tracker würde die
                // bereits gelaufenen Schritte aus der Anzeige werfen.
                stepTracker.getDailyGoal().set(goal);
            }

            stepOverflowLabel.setVisible(false);

            persist(() -> stepDB.setGoal(Session.getUserId(), goal),
                    "Could not save the goal.");
//            goalField.setPromptText("Current goal: " + goal); //Ziel-Feld:dauerhaft den gespeicherten Wert anzeigen

        } catch (NumberFormatException e) {
            showMessage("Please enter a valid step goal.");
        }
    }

    // -------------------------
    // ADD STEPS
    // -------------------------
    @FXML
    public void handleAddingSteps() {
        if (loading) {
            showMessage("Loading, please wait...");
            return;
        }
        if (stepTracker == null) {
            showMessage("Please set a goal first.");
            return;
        }

        try {
            int steps = Integer.parseInt(stepsField.getText());

            stepTracker.addSteps(steps);
            stepsField.clear(); // Zahl in "Steps done" wird verschwinden nach dem klick auf "Add Steps"
            stepOverflowLabel.setVisible(false);

            persist(() -> stepDB.addSteps(Session.getUserId(), steps, LocalDate.now()),
                    "Could not save the entry.");

        } catch (NegativeStepsException e) {
            showMessage("Steps must be a positive number!");

        } catch (NumberFormatException e) {
            showMessage("Please enter a valid number.");
        }
    }

    // -------------------------
    // RESET
    // -------------------------
    @FXML
    public void handleReset(ActionEvent event) {
        if (loading) {
            showMessage("Loading, please wait...");
            return;
        }
        if (stepTracker != null) {
            stepTracker.reset();
        }

        stepsField.setText("");
        stepOverflowLabel.setVisible(false);

        persist(() -> stepDB.resetSteps(Session.getUserId(), LocalDate.now()),
                "Could not reset the entries.");
    }

    @FXML
    public void handleBackToMenu(ActionEvent event) {
        changeView("mainMenu.fxml");
    }

    // Hilfsmethoden
    private void bindRemaining() {
        remainingField.textProperty().bind(
                stepTracker.remainingStepsProperty().asString()
        );
    }

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

    private void runInBackground(Task<?> task) {
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void showMessage(String message) {
        stepOverflowLabel.setText(message);
        stepOverflowLabel.setVisible(true);
    }

    @FunctionalInterface
    private interface DatabaseAction {
        void run() throws Exception;
    }
}