package com.example.cource_client;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class MainController {

    private final ServerService serverService = new ServerService();

    @FXML
    private Pane mapPane;

    @FXML
    private Button tc1Button, tc2Button, tc3Button, tc1Info, tc2Info, tc3Info, backButton, selectTc1Button, selectTc2Button, selectTc3Button;

    @FXML
    private void handleTc1Click() {
        changeBackground("/images/discovery.jpg");
        hideTcButtons();
        tc1Info.setVisible(true);
        selectTc1Button.setVisible(true);  // Показать кнопку "Выбрать этот ТЦ"
        backButton.setVisible(true);
    }

    @FXML
    private void handleTc2Click() {
        changeBackground("/images/vodniy.jpg");
        hideTcButtons();
        tc2Info.setVisible(true);
        selectTc2Button.setVisible(true);  // Показать кнопку "Выбрать этот ТЦ"
        backButton.setVisible(true);
    }

    @FXML
    private void handleTc3Click() {
        changeBackground("/images/aviapark.jpg");
        hideTcButtons();
        tc3Info.setVisible(true);
        selectTc3Button.setVisible(true);  // Показать кнопку "Выбрать этот ТЦ"
        backButton.setVisible(true);
    }

    @FXML
    private void showTc1Info() {
        showTcInfo("Дискавери", "Адрес: улица 1, 123", 10, 60);
    }

    @FXML
    private void showTc2Info() {
        showTcInfo("Водный", "Адрес: улица 2, 456", 15, 40);
    }

    @FXML
    private void showTc3Info() {
        showTcInfo("Авиапарк", "Адрес: улица 3, 789", 20, 100);
    }

    @FXML
    private void resetMap() {
        changeBackground("/images/map.jpg");
        tc1Button.setVisible(true);
        tc2Button.setVisible(true);
        tc3Button.setVisible(true);
        tc1Info.setVisible(false);
        tc2Info.setVisible(false);
        tc3Info.setVisible(false);
        selectTc1Button.setVisible(false);
        selectTc2Button.setVisible(false);
        selectTc3Button.setVisible(false);
        backButton.setVisible(false);
    }

    private void changeBackground(String imagePath) {
        Image image = new Image(getClass().getResource(imagePath).toExternalForm());

        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, false
                )
        );

        mapPane.setBackground(new Background(backgroundImage));
    }

    private void showTcInfo(String name, String address, int totalPlaces, int pricePerHour) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация о ТЦ");
        alert.setHeaderText(name);
        alert.setContentText(
                "Адрес: " + address + "\n" +
                "Всего мест: " + totalPlaces + "\n" +
                "Цена за час: " + pricePerHour + " руб."
        );
        alert.showAndWait();
    }

    private void hideTcButtons() {
        tc1Button.setVisible(false);
        tc2Button.setVisible(false);
        tc3Button.setVisible(false);
    }

    @FXML
    private void handleSelectTc1() {
        openParkingWindow(2, 5, "Дискавери");
    }

    @FXML
    private void handleSelectTc2() {
        openParkingWindow(3, 4, "Водный");
    }

    @FXML
    private void handleSelectTc3() {
        openParkingWindow(4, 5, "Авиапарк");
    }

    private void openParkingWindow(int rows, int columns, String tcName) {
        Stage parkingStage = new Stage();
        parkingStage.setTitle("Парковка " + tcName);

        GridPane parkingGrid = new GridPane();
        parkingGrid.setHgap(10);
        parkingGrid.setVgap(10);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Button spotButton = new Button("Место " + (i * columns + j + 1));
                spotButton.setPrefSize(80, 80);
                spotButton.setStyle("-fx-background-color: green;");

                spotButton.setOnAction(event -> {
                    openTimeSelectionWindow(spotButton.getText(), tcName, spotButton);
                });

                parkingGrid.add(spotButton, j, i);
            }
        }

        Scene scene = new Scene(parkingGrid, columns * 90, rows * 90);  // Размер окна в зависимости от мест
        parkingStage.setScene(scene);
        parkingStage.show();
    }

    private void openTimeSelectionWindow(String spotName, String tcName, Button spotButton) {
        Stage timeStage = new Stage();
        timeStage.setTitle("Выбор времени для " + spotName);

        VBox vbox = new VBox(15);
        vbox.setAlignment(Pos.CENTER);

        Label label = new Label("Выберите время аренды:");

        // Получаем текущее время
        LocalTime now = LocalTime.now();
        int currentHour = now.getHour();
        int currentMinute = now.getMinute();
        int maxTime = 22 * 60 + 59;  // Последний возможный момент дня

        // Ползунок для начала времени
        Slider startTimeSlider = new Slider(currentHour * 60 + currentMinute, maxTime, currentHour * 60 + currentMinute);
        startTimeSlider.setMinorTickCount(0);  // Убираем малые деления
        startTimeSlider.setShowTickLabels(false);  // Отключаем подписи
        startTimeSlider.setShowTickMarks(false);  // Отключаем отметки

        Label startTimeLabel = new Label("Начало: " + formatMinutes((int) startTimeSlider.getValue()));

        startTimeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            startTimeLabel.setText("Начало: " + formatMinutes(newVal.intValue()));
        });

        // Ползунок для конца времени
        Slider endTimeSlider = new Slider(currentHour * 60 + currentMinute, maxTime, currentHour * 60 + currentMinute + 30);
        endTimeSlider.setMinorTickCount(0);
        endTimeSlider.setShowTickLabels(false);
        endTimeSlider.setShowTickMarks(false);

        Label endTimeLabel = new Label("Конец: " + formatMinutes((int) endTimeSlider.getValue()));

        endTimeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            endTimeLabel.setText("Конец: " + formatMinutes(newVal.intValue()));
        });

        // Кнопка подтверждения выбора
        Button confirmButton = new Button("Подтвердить выбор");
        confirmButton.setOnAction(event -> {
            int startMinutes = (int) startTimeSlider.getValue();
            int endMinutes = (int) endTimeSlider.getValue();

            if (endMinutes <= startMinutes) {
                showError("Конечное время должно быть больше начального!");
            } else {
                System.out.println("Выбрано место: " + spotName);
                System.out.println("Время: с " + formatMinutes(startMinutes) + " до " + formatMinutes(endMinutes));

                // Получаем текущую дату
                LocalDate currentDate = LocalDate.now();

                // Создаём LocalTime из минут
                LocalTime startTime = LocalTime.of(startMinutes / 60, startMinutes % 60);
                LocalTime endTime = LocalTime.of(endMinutes / 60, endMinutes % 60);

                // Объединяем дату и время
                LocalDateTime startDateTime = LocalDateTime.of(currentDate, startTime);
                LocalDateTime endDateTime = LocalDateTime.of(currentDate, endTime);

                // Передаём LocalDateTime в openPaymentWindow
                timeStage.close();  // Закрываем окно времени
                openPaymentWindow(spotName, spotButton, tcName, startDateTime, endDateTime);  // Передаём время в оплату
            }
        });

        vbox.getChildren().addAll(label, startTimeSlider, startTimeLabel, endTimeSlider, endTimeLabel, confirmButton);

        Scene scene = new Scene(vbox, 400, 300);
        timeStage.setScene(scene);
        timeStage.show();
    }

    // Метод для форматирования времени в "HH:mm"
    private String formatMinutes(int totalMinutes) {
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openPaymentWindow(String spotId, Button spotButton, String tcName, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Long userId = 1L;  // Фиксированный ID пользователя
        int hourlyRate = 100;
        // Преобразуем spotId в номер места
        int spotNumber = Integer.parseInt(spotId.replace("Место ", ""));
        System.out.println(spotNumber);
        long durationMinutes = java.time.Duration.between(startDateTime, endDateTime).toMinutes();
        int amount = (int) ((durationMinutes / 60.0) * hourlyRate);

        Long transactionId = serverService.createTransaction(userId, amount);

        if (transactionId == null) {
            showError("Не удалось создать транзакцию.");
            return;
        }

        Stage paymentStage = new Stage();
        paymentStage.setOnCloseRequest(event -> {
            serverService.updateTransactionStatus(transactionId, "Отменена");
        });

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);

        Label label = new Label("Введите данные банковской карты:");

        // Поля для ввода данных карты
        TextField cardNumberField = new TextField();
        cardNumberField.setPromptText("Номер карты (16 цифр)");

        TextField expiryDateField = new TextField();
        expiryDateField.setPromptText("MM/YY");

        PasswordField cvvField = new PasswordField();
        cvvField.setPromptText("CVV (3 цифры)");

        Button payButton = new Button("Оплатить");
        payButton.setOnAction(event -> {
            String cardNumber = cardNumberField.getText().replaceAll("\\s+", "");
            String expiryDate = expiryDateField.getText();
            String cvv = cvvField.getText();

            if (isValidCard(cardNumber, expiryDate, cvv)) {
                System.out.println("Оплата прошла успешно для " + spotId);
                showSuccess("Оплата прошла успешно!");

                // Форматирование времени для отправки на сервер
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

                Long bookingId = serverService.sendBookingRequest(
                        spotNumber,
                        tcName,
                        startDateTime.format(formatter),
                        endDateTime.format(formatter)
                );

                if (bookingId != null) {
                    serverService.updateBookingId(transactionId, bookingId);
                }

                serverService.updateTransactionStatus(transactionId, "Подтверждена");

                spotButton.setStyle("-fx-background-color: red;");
                paymentStage.close();
            } else {
                showError("Ошибка: Проверьте введённые данные.");
            }
        });

        vbox.getChildren().addAll(label, cardNumberField, expiryDateField, cvvField, payButton);

        Scene scene = new Scene(vbox, 300, 250);
        paymentStage.setScene(scene);
        paymentStage.show();
    }

    private boolean isValidCard(String cardNumber, String expiryDate, String cvv) {
        // Проверка длины номера карты
        if (cardNumber.length() != 16 || !cardNumber.matches("\\d+")) {
            return false;
        }

        // Проверка формата даты (MM/YY)
        if (!expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            return false;
        }

        // Проверка длины CVV
        if (cvv.length() != 3 || !cvv.matches("\\d+")) {
            return false;
        }

        return true;
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успешная оплата");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
