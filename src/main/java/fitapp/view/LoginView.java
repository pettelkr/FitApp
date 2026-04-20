package com.fitapp.view;

import com.fitapp.controller.LoginController;
import com.fitapp.navigation.Navigator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginView extends Application {

    private Stage stage; // store stage for testing

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.stage = primaryStage;

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/fxml/login.fxml"));

        Parent root = loader.load();

        LoginController controller = loader.getController();
        controller.setNavigator(new Navigator(primaryStage));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.show();
    }

    // Expose stage for tests
    public Stage getStage() {
        return stage;
    }
}


