package org.example.course_client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/course_client/MainView.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Parking Client Application");
        primaryStage.setScene(new Scene(root, 800, 600));  // Установим фиксированные размеры
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
