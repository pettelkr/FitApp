package com.fitapp.controller;

import com.fitapp.navigation.Navigator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.assertions.api.Assertions.assertThat;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

class LoginControllerTest extends ApplicationTest {

    private LoginController logCon;
    private Stage stage;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label errorLabel;

    @Override
    public void start(Stage stage) throws Exception {
        // store the stage
        this.stage = stage;
        // Set up the actual scene for the LoginController
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = loader.load();

        // Inject the controller into the loaded scene
        logCon = loader.getController();

        // Inject a real Navigator
        logCon.setNavigator(new Navigator(stage));

        // Attach the scene and display it
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    void setUp() {
        // Query the fields and labels by their IDs
        usernameField = lookup("#usernameField").query();  // query by id
        passwordField = lookup("#passwordField").query();  // query by id
        errorLabel = lookup("#errorLabel").query();  // query by id
    }

    @Test
    void testLoginSuccess() {
        // Arrange: Simulate user input
        clickOn(usernameField).write("Rene");
        clickOn(passwordField).write("1234");

        // Act: Simulate clicking the login button
        clickOn("#loginButton");
        // Ensure all FX events have been processed
        waitForFxEvents();
        // Assert: Check that the error label is not visible and the scene changed
        assertThat(errorLabel.isVisible()).isFalse();
    }

    @Test
    void testLoginFailure() {
        // Arrange: Simulate user input with invalid credentials
        clickOn(usernameField).write("user");
        clickOn(passwordField).write("password");

        // Act: Simulate clicking the login button
        clickOn("#loginButton");
        // Ensure all FX events have been processed
        waitForFxEvents();
        // Assert: Check that the error label is visible with the correct error message
        assertThat(errorLabel.isVisible());
        assertThat(errorLabel.getText()).isEqualTo("Login Failed");
    }

    @Test
    void testChangeView(){

        // Run on JavaFX thread
        interact(() -> {logCon.changeView("mainMenu.fxml"); // the FXML you want to switch to
        });

        // Check that the stage title has changed
        assertEquals("Menu", stage.getTitle());

        // Check that the root node is not null (scene has changed)
        assertNotNull(stage.getScene().getRoot());

        // check for the type of root node
        assertTrue(stage.getScene().getRoot() instanceof Parent);

    }

}




