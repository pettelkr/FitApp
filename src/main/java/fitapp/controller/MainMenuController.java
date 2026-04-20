package com.fitapp.controller;

import java.io.IOException;

import com.fitapp.navigation.Navigator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class MainMenuController implements Controller {

    private Navigator navigator;

    // default constructor for FXML loading
    public MainMenuController(){

    }

    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void changeView(String fxmlFile) {
        navigator.changeView(fxmlFile);
    }

    @FXML
    private Button caloriesButton;

    @FXML
    public void handleCheckCalories(ActionEvent event){
        changeView("caloricIntake.fxml");
    }


}
