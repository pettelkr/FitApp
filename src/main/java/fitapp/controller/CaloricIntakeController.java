package com.fitapp.controller;

import com.fitapp.model.CaloriesTracker;
import java.io.IOException;
import com.fitapp.model.NegativeCaloriesException;
import com.fitapp.model.CalorieLimitExceededException;
import com.fitapp.navigation.Navigator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class CaloricIntakeController implements Controller {
    /**
     * This controller class handles the functionalities for adding calories, resetting the caloric intake
     * and enabling to get back to the main menu.
     * */

    private Navigator navigator;

    // default constructor for FXML loading
    public CaloricIntakeController(){

    }

    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void changeView(String fxmlFile) {
        navigator.changeView(fxmlFile);
    }


    // instantiating CaloriesTracker object with recommended calories for an average adult
    private CaloriesTracker calTra = new CaloriesTracker(2000);

    @FXML
    private TextField caloriesField;

    @FXML
    private TextField remainingField;

    @FXML
    private Label caloriesOverflowLabel;

    @FXML
    public void handleAddingCalories() {
        try {
            int calories = Integer.parseInt(caloriesField.getText());
            calTra.addCalories(calories);
            // observer
            remainingField.textProperty().bind(calTra.remainingCaloriesProperty().asString());
            caloriesOverflowLabel.setVisible(false);
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

    @FXML
    public void handleReset(ActionEvent event) {
        calTra.reset();
        // observer
        remainingField.textProperty().bind(calTra.getDailyLimit().asString());
        caloriesField.setText("");
        caloriesOverflowLabel.setVisible(false);
    }

    @FXML
    public void handleBackToMenu(ActionEvent event){
        changeView("mainMenu.fxml");
    }


}
