package com.example.course_client.controller;

import com.example.course_client.dto.BookingDTO;
import com.example.course_client.service.ServerService;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.course_client.TCInfo;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainController {

    private final ServerService serverService = new ServerService();
    private String userType;

    // Логин
    @FXML
    private VBox loginPane;
    @FXML
    private TextField loginUsernameField;
    @FXML
    private PasswordField loginPasswordField;
    @FXML
    private Label loginErrorLabel;

    // Регистрация
    @FXML
    private VBox registerPane;
    @FXML
    private TextField registerUsernameField, registerEmailField;
    @FXML
    private PasswordField registerPasswordField;
    @FXML
    private PasswordField registerConfirmPasswordField;
    @FXML
    private Label registerErrorLabel;

    // Главный экран
    @FXML
    private Pane mapPane, mainPane;
    @FXML
    private Button tc1Button, tc2Button, tc3Button, tc1Info,
            tc2Info, tc3Info, backButton, selectTc1Button,
            selectTc2Button, selectTc3Button, toAdminPanelButton;

    @FXML
    public void initialize() {
        // Скрываем карту при запуске, чтобы сначала отображалась форма логина
        mapPane.setVisible(false);
    }

    @FXML
    private void switchToAdminPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/course_client/AdminPanel.fxml"));
            Parent adminRoot = loader.load();

            // Получаем контроллер AdminController
            AdminController adminController = loader.getController();
            adminController.setMainController(this); // Передаём ссылку на MainController

            Stage adminStage = new Stage(); // Создаём новый Stage
            adminStage.setTitle("Админ-панель");
            adminStage.setScene(new Scene(adminRoot, 800, 600));
            adminStage.show();
            switchToMain();

            // Сохраняем ссылку на админский Stage для дальнейшего управления

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


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

    private final List<TCInfo> tcInfoList = List.of(
            new TCInfo("Дискавери", "улица Дыбенко, 7/1", 10, 40),
            new TCInfo("Водный", "Головинское ш., 5, корп. 1", 12, 80),
            new TCInfo("Авиапарк", "Ходынский бульвар, 4", 20, 120)
    );

    @FXML
    private void showTc1Info() {
        showTcInfo("Дискавери");
    }

    @FXML
    private void showTc2Info() {
        showTcInfo("Водный");
    }

    @FXML
    private void showTc3Info() {
        showTcInfo("Авиапарк");
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

    public void changeBackground(String imagePath) {
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

    @FXML
    private void showTcInfo(String tcName) {
        TCInfo tcInfo = tcInfoList.stream()
                .filter(tc -> tc.getName().equals(tcName))
                .findFirst()
                .orElse(null);

        if (tcInfo != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация о ТЦ");
            alert.setHeaderText(tcInfo.getName());
            alert.setContentText(
                    "Адрес: " + tcInfo.getAddress() + "\n" +
                            "Всего мест: " + tcInfo.getTotalSpots() + "\n" +
                            "Цена за час: " + tcInfo.getPricePerHour() + " руб."
            );
            alert.showAndWait();
        } else {
            showError("Информация о ТЦ не найдена!");
        }
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

        Map<Button, Integer> spotButtons = new HashMap<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int spotNumber = i * columns + j + 1;
                Button spotButton = new Button("Место " + spotNumber);
                spotButton.setPrefSize(80, 80);
                spotButton.setStyle("-fx-background-color: green;");

                spotButton.setOnAction(event -> {
                    openTimeSelectionWindow(spotButton.getText(), tcName, spotButton);
                });

                parkingGrid.add(spotButton, j, i);
                spotButtons.put(spotButton, spotNumber);
            }
        }
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            updateSpotStatuses(spotButtons, tcName);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        Scene scene = new Scene(parkingGrid, columns * 90, rows * 90);  // Размер окна в зависимости от мест
        parkingStage.setScene(scene);
        parkingStage.show();
    }

    private void updateSpotStatuses(Map<Button, Integer> spotButtons, String tcName) {
        List<BookingDTO> bookings = serverService.fetchTodayBookings();

        for (Map.Entry<Button, Integer> entry : spotButtons.entrySet()) {
            Button button = entry.getKey();
            int spotNumber = entry.getValue();

            // Запрашиваем статус с сервера
            String status = serverService.getSpotStatus(tcName, spotNumber);

            BookingDTO currentBooking = bookings.stream()
                    .filter(booking -> booking.getTcName().equals(tcName)
                            && booking.getSpotNumber() == spotNumber
                            && !"COMPLETED".equals(booking.getStatus()))
                    .findFirst()
                    .orElse(null);

            String endTimeMessage = (currentBooking != null)
                    ? " (освободится в " + currentBooking.getEndTime() + ")"
                    : "";

            // Меняем цвет кнопки в зависимости от статуса
            switch (status) {
                case "RESERVED" -> {
                    button.setStyle("-fx-background-color: orange;"); // Отложено
                    button.setOnAction(event -> showError("Место зарезервировано" + endTimeMessage + " и недоступно для бронирования."));
                }
                case "IN_PROGRESS" -> {
                    button.setStyle("-fx-background-color: red;"); // В процессе
                    button.setOnAction(event -> showError("Место занято" + endTimeMessage + " и недоступно для бронирования."));
                }
                case "COMPLETED" -> {
                    button.setStyle("-fx-background-color: green;"); // Завершено (выделено отдельным цветом)
                    button.setDisable(false); // Некликабельно
                }
                default -> {
                    button.setStyle("-fx-background-color: green;"); // Свободно
                    button.setDisable(false); // Кликабельно
                }
            }
        }
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

    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openPaymentWindow(String spotId, Button spotButton, String tcName, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Long userId = serverService.getId(); // Фиксированный ID пользователя
        TCInfo tcInfo = tcInfoList.stream()
                .filter(tc -> tc.getName().equals(tcName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Информация о ТЦ не найдена: " + tcName));
        int hourlyRate = tcInfo.getPricePerHour(); // Берём цену за час из данных о ТЦ
        int spotNumber = Integer.parseInt(spotId.replace("Место ", ""));
        long durationMinutes = java.time.Duration.between(startDateTime, endDateTime).toMinutes();
        int amount = (int) ((durationMinutes / 60.0) * hourlyRate);

        Long transactionId = serverService.createTransaction(userId, amount);

        if (transactionId == null) {
            showError("Не удалось создать транзакцию.");
            return;
        }

        Stage paymentStage = new Stage();
        paymentStage.setOnCloseRequest(event -> {
            serverService.updateTransactionStatus(transactionId, "CANCELLED");
        });

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);

        Label label = new Label("Введите данные банковской карты:");
        Label amountLabel = new Label("К оплате: " + amount + " руб.");

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
                showSuccess("Оплата прошла успешно!");

                // Форматирование времени для отправки на сервер
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

                Long bookingId = serverService.sendBookingRequest(
                        userId,
                        spotNumber,
                        tcName,
                        startDateTime.format(formatter),
                        endDateTime.format(formatter)
                );

                if (bookingId != null) {
                    serverService.updateBookingId(transactionId, bookingId);
                }

                serverService.updateTransactionStatus(transactionId, "APPROVED");

                paymentStage.close();
            } else {
                showError("Ошибка: Проверьте введённые данные.");
            }
        });

        vbox.getChildren().addAll(label, amountLabel,cardNumberField, expiryDateField, cvvField, payButton);

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
        return cvv.length() == 3 && cvv.matches("\\d+");
    }

    public void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успешно!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ======= ЛОГИН =======
    @FXML
    private void handleLogin() {
        String username = loginUsernameField.getText();
        String password = loginPasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            loginErrorLabel.setText("Пожалуйста, заполните все поля.");
            return;
        }

        userType = serverService.login(username, password);

        if (userType != null) {
            loginErrorLabel.setText("");
            if ("ADMIN".equals(userType)) {
                switchToAdminPanel();
            } else {
                switchToMain();
            }
        } else {
            loginErrorLabel.setText("Ошибка логина. Проверьте данные.");
        }
    }

    // ======= РЕГИСТРАЦИЯ =======
    @FXML
    private void handleRegister() {
        String username = registerUsernameField.getText();
        String email = registerEmailField.getText();
        String password = registerPasswordField.getText();
        String confirmPassword = registerConfirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            registerErrorLabel.setText("Пожалуйста, заполните все поля.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            registerErrorLabel.setText("Пароли не совпадают.");
            return;
        }

        boolean success = serverService.register(username, email, password);
        if (success) {
            showSuccess("Регистрация успешна! Теперь войдите.");
            switchToLogin();
        } else {
            registerErrorLabel.setText("Ошибка регистрации. Попробуйте другой логин.");
        }
    }

    @FXML
    private void handleLogout() {
        serverService.setToken(null); // Удаляем токен
        serverService.setId(null);
        toAdminPanelButton.setVisible(false);
        switchToLogin();
    }

    @FXML
    private void switchToLogin() {
        registerPane.setVisible(false);
        registerPane.setManaged(false);
        loginPane.setVisible(true);
        loginPane.setManaged(true);
        mapPane.setVisible(false);
        mapPane.setManaged(false);
        // Очищаем поля логина
        loginUsernameField.clear();
        loginPasswordField.clear();
        loginErrorLabel.setText("");

    }

    @FXML
    private void switchToRegister() {
        loginPane.setVisible(false);
        loginPane.setManaged(false);
        registerPane.setVisible(true);
        registerPane.setManaged(true);
    }

    @FXML
    public void switchToMain() {
        resetMap();
        loginPane.setVisible(false);
        loginPane.setManaged(false);
        registerPane.setVisible(false);
        registerPane.setManaged(false);
        mapPane.setVisible(true);
        mapPane.setManaged(true);
        if ("ADMIN".equals(userType)) {
            toAdminPanelButton.setVisible(true); // Показываем кнопку для админа
        }
    }

    @FXML
    private void showAboutAuthor() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Об авторе");
        alert.setHeaderText("Курсовая работа по JavaFX");
        alert.setContentText("""
            Автор: Косарев Григорий
            Группа: ПИ22-1
            Почта: 222724@edu.fa.ru
            Преподаватель: Свирина А.Г.
            Версия приложения: 1.0
            Все права защищены.
            """);
        alert.showAndWait();
    }

}