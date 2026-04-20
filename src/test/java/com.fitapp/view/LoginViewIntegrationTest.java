package com.fitapp.view;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginViewIntegrationTest extends ApplicationTest {

    private LoginView loginView;

    @Override
    public void start(Stage stage) throws Exception {
        loginView = new LoginView();
        loginView.start(stage); // launch the view
    }

    @Test
    void testLoginViewLaunches() {
        Stage stage = loginView.getStage();
        assertNotNull(stage);
        assertEquals("Login", stage.getTitle());
        assertNotNull(stage.getScene().getRoot());
    }
}

