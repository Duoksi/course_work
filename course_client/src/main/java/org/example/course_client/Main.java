package org.example.course_client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

/**
 * Главный класс приложения для клиента парковки.
 * Осуществляет запуск JavaFX приложения и отображение главного окна.
 */
public class Main extends Application {

    /**
     * Метод, который запускает приложение и отображает главное окно.
     *
     * @param primaryStage основной этап JavaFX приложения.
     * @throws Exception если возникла ошибка при загрузке FXML или установке сцены.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/course_client/MainView.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Parking Client Application");
        primaryStage.setScene(new Scene(root, 800, 600));  // Установим фиксированные размеры
        primaryStage.show();
    }

    /**
     * Главный метод для запуска приложения.
     *
     * @param args аргументы командной строки.
     */
    public static void main(String[] args) {
        launch(args);
    }
}