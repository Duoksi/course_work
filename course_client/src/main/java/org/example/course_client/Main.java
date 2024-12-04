package org.example.course_client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/course_client/MainView.fxml"));
        Parent root = loader.load();

        // Проверка пути к изображению
        String img = getClass().getResource("/images/map.jpg").toExternalForm();

        // Загрузка изображения и вывод его размеров
        Image backgroundImage = new Image(img);

        // Визуальное добавление ImageView для проверки отображения изображения
        ImageView imageView = new ImageView(backgroundImage);
        ((StackPane) root).getChildren().add(0, imageView);  // Добавим на фон

        primaryStage.setTitle("Parking Client Application");
        primaryStage.setScene(new Scene(root, backgroundImage.getWidth(), backgroundImage.getHeight()));  // Установим фиксированные размеры
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}