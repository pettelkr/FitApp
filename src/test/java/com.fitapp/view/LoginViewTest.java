package com.fitapp.view;

import static org.junit.jupiter.api.Assertions.*;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

public class LoginViewTest extends ApplicationTest {

    private LoginView loginView;

    @Override
    public void start(Stage stage) throws Exception {
        // Launch LoginView
        loginView = new LoginView();
        loginView.start(stage); // normal start, sets internal stage
    }

    @Test
    void testStageAndScene() {
        Stage stage = loginView.getStage(); // now directly accessible
        assertNotNull(stage);

        Scene scene = stage.getScene();
        assertNotNull(scene);

        Parent root = scene.getRoot();
        assertNotNull(root);

        assertEquals("Login", stage.getTitle());
    }

    @Test
    void testControlsExist() {
        Parent root = loginView.getStage().getScene().getRoot();

        TextField username = (TextField) root.lookup("#usernameField");
        PasswordField password = (PasswordField) root.lookup("#passwordField");
        Label errorLabel = (Label) root.lookup("#errorLabel");

        assertNotNull(username);
        assertNotNull(password);
        assertNotNull(errorLabel);
    }
}
