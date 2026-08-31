package com.fitapp.controller;
import com.fitapp.model.*;
import com.fitapp.navigation.Navigator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class LoginController implements Controller {

    private Navigator navigator;
    private UserRepository userDB = new UserDatabaseSQLite();

    @FXML private VBox loginPanel;
    @FXML private VBox registerPanel;

    // Login Felder
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    // Register Felder
    @FXML private TextField regUsernameField;
    @FXML private PasswordField regPasswordField;
    @FXML private PasswordField regConfirmField;
    @FXML private Label regErrorLabel;

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

    // ── Panel wechseln ──────────────────────────────
    @FXML
    public void showRegister() {
        loginPanel.setVisible(false);
        registerPanel.setVisible(true);
    }

    @FXML
    public void showLogin() {
        loginPanel.setVisible(true);
        registerPanel.setVisible(false);
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        try {
            String username = usernameField.getText();

            userDB.validateInput(username, passwordField.getText());

            Session.login(((UserDatabaseSQLite) userDB).findIdByUsername(username), username);

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

    @FXML
    public void handleRegister() {
        String username = regUsernameField.getText();
        String password = regPasswordField.getText();
        String confirm  = regConfirmField.getText();

        if (username.isBlank() || password.isBlank()) {
            regErrorLabel.setText("Bitte alle Felder ausfüllen!");
            regErrorLabel.setVisible(true);
            return;
        }
        if (!password.equals(confirm)) {
            regErrorLabel.setText("Passwörter stimmen nicht überein!");
            regErrorLabel.setVisible(true);
            return;
        }

        if (((UserDatabaseSQLite) userDB).addUser(username, password)) {
            showLogin();
        } else {
            regErrorLabel.setText("Benutzername ist schon vergeben!");
            regErrorLabel.setVisible(true);
        }
    }

}