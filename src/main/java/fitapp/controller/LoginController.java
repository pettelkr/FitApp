package com.fitapp.controller;
import com.fitapp.navigation.Navigator;

import com.fitapp.model.EmptyFieldException;
import com.fitapp.model.InvalidCredentialsException;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.fitapp.model.UserDatabaseCSV;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;

public class LoginController implements Controller {

    private Navigator navigator;

    // default constructor for FXML loading
    public LoginController(){

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
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    private UserDatabaseCSV userDB = new UserDatabaseCSV();

    @FXML
    public void handleLogin(ActionEvent event) {
        try {
            String user = usernameField.getText();
            String pass = passwordField.getText();

            userDB.validateInput(user, pass);

            errorLabel.setVisible(false);
            changeView("mainMenu.fxml");

        } catch (EmptyFieldException e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);

        } catch (InvalidCredentialsException e) {
            errorLabel.setText("Login Failed");
            errorLabel.setVisible(true);
        }
    }

}