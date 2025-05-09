package com.example.course_client.controller;

import com.example.course_client.dto.*;
import com.example.course_client.service.ServerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.*;
import java.util.*;

/**
 * Контроллер для админ-панели.
 * Управляет отображением и взаимодействием с данными, такими как пользователи, парковочные места, бронирования и транзакции.
 * Обеспечивает взаимодействие с сервером для получения и обновления данных.
 */
public class AdminController {

    private final String SERVER_URL = "http://localhost:8080"; // URL сервера
    private final ServerService serverService = new ServerService(); // Сервис для работы с сервером
    private final HttpClient httpClient = HttpClient.newHttpClient(); // HTTP клиент для отправки запросов
    private final String token = serverService.getToken(); // Токен для аутентификации
    private MainController mainController; // Главный контроллер

    /**
     * Устанавливает основной контроллер для переключения между окнами.
     *
     * @param mainController Основной контроллер
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private StackPane tablePane; // Панель таблицы

    /**
     * Переключение на главную страницу.
     * Закрывает окно админ-панели и возвращает на главную страницу.
     */
    @FXML
    private void switchToMain() {
        if (mainController != null) {
            mainController.switchToMain(); // Переключаемся на карту через MainController
            Stage stage = (Stage) tablePane.getScene().getWindow();
            stage.close(); // Закрываем текущую панель
        } else {
            System.err.println("Ошибка: MainController не установлен.");
        }
    }

    // Объявление полей для поиска и создания пользователей, а также таблиц для отображения данных
    @FXML
    private TextField searchUserField;
    @FXML
    private TextField searchIdField;
    @FXML
    private VBox createUserPane;
    @FXML
    private TextField usernameField, emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ChoiceBox<String> roleChoiceBox;
    @FXML
    private TableView<UserDTO> usersTable;
    @FXML
    private TableColumn<UserDTO, Long> userIdColumn;
    @FXML
    private TableColumn<UserDTO, String> usernameColumn;
    @FXML
    private TableColumn<UserDTO, String> passwordColumn;
    @FXML
    private TableColumn<UserDTO, String> emailColumn;
    @FXML
    private TableColumn<UserDTO, String> userTypeColumn;
    @FXML
    private TableView<ParkingSpotDTO> parkingSpotsTable;
    @FXML
    private TableColumn<ParkingSpotDTO, Long> parkingSpotIdColumn;
    @FXML
    private TableColumn<ParkingSpotDTO, Integer> spotNumberColumn;
    @FXML
    private TableColumn<ParkingSpotDTO, String> tcNameColumn;
    @FXML
    private TableView<BookingDTO> bookingsTable;
    @FXML
    private TableColumn<BookingDTO, Long> bookingIdColumn;
    @FXML
    private TableColumn<BookingDTO, Long> bookingUserIdColumn;
    @FXML
    private TableColumn<BookingDTO, String> bookingTcNameColumn;
    @FXML
    private TableColumn<BookingDTO, Integer> bookingSpotNumberColumn;
    @FXML
    private TableColumn<BookingDTO, String> bookingTimeColumn;
    @FXML
    private TableColumn<BookingDTO, String> startTimeColumn;
    @FXML
    private TableColumn<BookingDTO, String> endTimeColumn;
    @FXML
    private TableColumn<BookingDTO, String> bookingStatusColumn;
    @FXML
    private TableView<TransactionDTO> transactionsTable;
    @FXML
    private TableColumn<TransactionDTO, Long> transactionIdColumn;
    @FXML
    private TableColumn<TransactionDTO, Long> transactionBookingIdColumn;
    @FXML
    private TableColumn<TransactionDTO, Long> transactionUserIdColumn;
    @FXML
    private TableColumn<TransactionDTO, String> transactionTimeColumn;
    @FXML
    private TableColumn<TransactionDTO, Integer> amountColumn;
    @FXML
    private TableColumn<TransactionDTO, String> transactionStatusColumn;

    /**
     * Инициализация контроллера, настройка таблиц и обработчиков событий.
     */
    @FXML
    private void initialize() {
        // Настройка таблицы пользователей
        userIdColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        usernameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUsername()));
        passwordColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPassword()));
        emailColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        userTypeColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUserType()));

        // Настройка таблицы парковочных мест
        parkingSpotIdColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        spotNumberColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getSpotNumber()));
        tcNameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTcName()));

        // Настройка таблицы бронирований
        bookingIdColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        bookingUserIdColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getUserId()));
        bookingSpotNumberColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getSpotNumber()));
        bookingTcNameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTcName()));
        bookingTimeColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getBookingTime()));
        startTimeColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getStartTime()));
        endTimeColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getEndTime()));
        bookingStatusColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));

        // Настройка таблицы транзакций
        transactionIdColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        transactionBookingIdColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getBookingId()));
        transactionUserIdColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getUserId()));
        transactionTimeColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getTransactionTime()));
        amountColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getAmount()));
        transactionStatusColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));

        // Обработчики для двойного щелчка на строках таблиц
        usersTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                UserDTO selectedUser = usersTable.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    handleDeleteUser(selectedUser); // Обработка удаления пользователя
                }
            }
        });

        bookingsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                BookingDTO selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
                if (selectedBooking != null) {
                    handleChangeBookingStatus(selectedBooking); // Изменение статуса бронирования
                }
            }
        });

        transactionsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TransactionDTO selectedTransaction = transactionsTable.getSelectionModel().getSelectedItem();
                if (selectedTransaction != null) {
                    handleChangeTransactionStatus(selectedTransaction); // Изменение статуса транзакции
                }
            }
        });
    }

    /**
     * Получение списка пользователей с сервера.
     *
     * @return Список пользователей
     */
    public List<UserDTO> getUsers() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(SERVER_URL + "/api/users"))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), new TypeReference<List<UserDTO>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Получение списка парковочных мест с сервера.
     *
     * @return Список парковочных мест
     */
    public List<ParkingSpotDTO> getParkingSpots() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(SERVER_URL + "/api/parking-spots"))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), new TypeReference<List<ParkingSpotDTO>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Получение списка бронирований с сервера.
     *
     * @return Список бронирований
     */
    public List<BookingDTO> getBookings() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(SERVER_URL + "/api/bookings"))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), new TypeReference<List<BookingDTO>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Получение списка транзакций с сервера.
     *
     * @return Список транзакций
     */
    public List<TransactionDTO> getTransactions() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(SERVER_URL + "/api/transactions"))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), new TypeReference<List<TransactionDTO>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Показывает только указанный узел (Node) и скрывает все остальные.
     * Этот метод используется для переключения видимости между различными панелями управления.
     *
     * @param node Узел, который нужно отобразить.
     */
    private void showOnly(Node node) {
        usersTable.setVisible(false);
        usersTable.setManaged(false);
        createUserPane.setVisible(false);
        createUserPane.setManaged(false);
        bookingsTable.setVisible(false);
        bookingsTable.setManaged(false);
        parkingSpotsTable.setVisible(false);
        parkingSpotsTable.setManaged(false);
        transactionsTable.setVisible(false);
        transactionsTable.setManaged(false);

        node.setVisible(true);
        node.setManaged(true);
    }

    /**
     * Отображает таблицу пользователей и скрывает другие компоненты.
     * Загружает список пользователей с сервера и отображает его в таблице.
     */
    @FXML
    private void showUsersTable() {
        showOnly(usersTable);
        createUserPane.setVisible(false);
        createUserPane.setManaged(false);
        List<UserDTO> users = getUsers();
        usersTable.getItems().setAll(users);
    }

    /**
     * Отображает форму для создания нового пользователя и скрывает другие компоненты.
     */
    @FXML
    private void showCreateUserForm() {
        showOnly(createUserPane);
    }

    /**
     * Создаёт нового пользователя с заданными параметрами и добавляет его в систему.
     * Если создание пользователя прошло успешно, обновляется таблица пользователей.
     */
    @FXML
    private void createUser() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String userType = roleChoiceBox.getValue();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || userType == null) {
            mainController.showError("Заполните все поля!");
            return;
        }

        UserDTO newUser = new UserDTO(username, email, password, userType);
        boolean success = serverService.createUser(newUser);
        if (success) {
            showUsersTable();
            mainController.showSuccess("Пользователь успешно создан!");
        } else {
            mainController.showError("Ошибка при создании пользователя.");
        }
    }

    /**
     * Отображает таблицу бронирований и скрывает другие компоненты.
     * Загружает список бронирований с сервера и отображает его в таблице.
     */
    @FXML
    private void showBookingsTable() {
        showOnly(bookingsTable);
        List<BookingDTO> bookings = getBookings();
        bookingsTable.getItems().setAll(bookings);
    }

    /**
     * Отображает таблицу парковочных мест и скрывает другие компоненты.
     * Загружает список парковочных мест с сервера и отображает его в таблице.
     */
    @FXML
    private void showParkingSpotsTable() {
        showOnly(parkingSpotsTable);
        List<ParkingSpotDTO> spots = getParkingSpots();
        parkingSpotsTable.getItems().setAll(spots);
    }

    /**
     * Отображает таблицу транзакций и скрывает другие компоненты.
     * Загружает список транзакций с сервера и отображает его в таблице.
     */
    @FXML
    private void showTransactionsTable() {
        showOnly(transactionsTable);
        List<TransactionDTO> transactions = getTransactions();
        transactionsTable.getItems().setAll(transactions);
    }

    /**
     * Обрабатывает удаление пользователя. Показывает подтверждение удаления и, если пользователь подтверждает,
     * удаляет его из системы.
     *
     * @param user Пользователь, которого нужно удалить.
     */
    private void handleDeleteUser(UserDTO user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение удаления");
        alert.setHeaderText("Удаление пользователя");
        alert.setContentText("Вы уверены, что хотите удалить пользователя " + user.getUsername() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = serverService.deleteUser(user.getId());
            if (success) {
                usersTable.getItems().remove(user); // Удаляем из таблицы
                mainController.showSuccess("Пользователь удалён!");
            } else {
                mainController.showError("Ошибка при удалении пользователя.");
            }
        }
    }

    /**
     * Обрабатывает изменение статуса бронирования. Показывает диалог для выбора нового статуса
     * и обновляет статус на сервере.
     *
     * @param booking Бронирование, статус которого нужно изменить.
     */
    private void handleChangeBookingStatus(BookingDTO booking) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("RESERVED", "RESERVED", "IN_PROGRESS", "CANCELLED", "COMPLETED");
        dialog.setTitle("Изменение статуса");
        dialog.setHeaderText("Изменение статуса бронирования");
        dialog.setContentText("Выберите новый статус для бронирования:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String newStatus = result.get();
            boolean success = serverService.updateBookingStatus(booking.getId(), newStatus);
            if (success) {
                booking.setStatus(newStatus);
                bookingsTable.refresh(); // Обновляем таблицу
                mainController.showSuccess("Статус бронирования изменён!");
            } else {
                mainController.showError("Ошибка при изменении статуса.");
            }
        }
    }

    /**
     * Обрабатывает изменение статуса транзакции. Показывает диалог для выбора нового статуса
     * и обновляет статус на сервере.
     *
     * @param transaction Транзакция, статус которой нужно изменить.
     */
    private void handleChangeTransactionStatus(TransactionDTO transaction) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("APPROVED", "APPROVED", "IN_PROGRESS", "CANCELLED");
        dialog.setTitle("Изменение статуса");
        dialog.setHeaderText("Изменение статуса транзакции");
        dialog.setContentText("Выберите новый статус для транзакции:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String newStatus = result.get();
            boolean success = serverService.updateTransactionStatus(transaction.getId(), newStatus);
            if (success) {
                transaction.setStatus(newStatus);
                transactionsTable.refresh(); // Обновляем таблицу
                mainController.showSuccess("Статус транзакции изменён!");
            } else {
                mainController.showError("Ошибка при изменении статуса.");
            }
        }
    }

    /**
     * Ищет пользователя по имени (username). Если пользователь найден, отображает его в таблице.
     */
    @FXML
    private void searchUserByUsername() {
        String username = searchUserField.getText().trim();
        if (username.isEmpty()) {
            mainController.showError("Введите username для поиска.");
            return;
        }

        List<UserDTO> users = serverService.findUserByUsername(username);
        if (users.isEmpty()) {
            mainController.showError("Пользователи с именем \"" + username + "\" не найдены.");
        } else {
            usersTable.setItems(FXCollections.observableArrayList(users));
        }
    }

    /**
     * Ищет бронирования по ID пользователя. Если бронирования найдены, отображает их в таблице.
     */
    @FXML
    private void searchBookingsByUserId() {
        String userIdText = searchIdField.getText().trim();
        if (userIdText.isEmpty()) {
            mainController.showError("Введите ID пользователя для поиска.");
            return;
        }

        try {
            Long userId = Long.parseLong(userIdText);
            List<BookingDTO> bookings = serverService.findBookingsByUserId(userId);
            if (bookings.isEmpty()) {
                mainController.showError("Бронирования для пользователя с ID " + userId + " не найдены.");
            } else {
                bookingsTable.setItems(FXCollections.observableArrayList(bookings));
            }
        } catch (NumberFormatException e) {
            mainController.showError("ID пользователя должен быть числом.");
        }
    }

    /**
     * Ищет транзакции по ID пользователя. Если транзакции найдены, отображает их в таблице.
     */
    @FXML
    private void searchTransactionsByUserId() {
        String userIdText = searchIdField.getText().trim();
        if (userIdText.isEmpty()) {
            mainController.showError("Введите ID пользователя для поиска.");
            return;
        }

        try {
            Long userId = Long.parseLong(userIdText);
            List<TransactionDTO> transactions = serverService.findTransactionsByUserId(userId);
            if (transactions.isEmpty()) {
                mainController.showError("Транзакции для пользователя с ID " + userId + " не найдены.");
            } else {
                transactionsTable.setItems(FXCollections.observableArrayList(transactions));
            }
        } catch (NumberFormatException e) {
            mainController.showError("ID пользователя должен быть числом.");
        }
    }

    /**
     * Показывает информацию об авторе приложения.
     */
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

    /**
     * Показывает статистику по парковочным местам для выбранных ТЦ.
     */
    @FXML
    private void showStatistics() {
        Map<String, int[]> statistics = fetchParkingStatistics();
        StringBuilder statsText = new StringBuilder("Статистика по ТЦ:\n\n");

        // Упорядочиваем по фиксированному порядку
        List<String> orderedTcNames = List.of("Дискавери", "Водный", "Авиапарк");
        Map<String, int[]> sortedStatistics = new LinkedHashMap<>();

        for (String tcName : orderedTcNames) {
            if (statistics.containsKey(tcName)) {
                sortedStatistics.put(tcName, statistics.get(tcName));
            }
        }

        // Генерация текста
        for (Map.Entry<String, int[]> entry : sortedStatistics.entrySet()) {
            String tcName = entry.getKey();
            int[] stats = entry.getValue(); // [занятых мест, всего мест]
            statsText.append(String.format("ТЦ: %s\nЗанято: %d из %d мест\n\n", tcName, stats[0], stats[1]));
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Статистика парковок");
        alert.setHeaderText("Информация по занятости мест");
        alert.setContentText(statsText.toString());
        alert.showAndWait();
    }

    /**
     * Получает статистику по парковочным местам с сервера.
     *
     * @return Карта с данными по ТЦ и их парковочным местам.
     */
    private Map<String, int[]> fetchParkingStatistics() {
        Map<String, int[]> statistics = new HashMap<>();
        try {
            // Отправляем запрос на сервер для получения статистики
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/parking-spots/parking-statistics"))
                    .header("Authorization", "Bearer " + serverService.getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = serverService.getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            statistics = objectMapper.readValue(response.body(), new TypeReference<>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            mainController.showError("Ошибка получения статистики с сервера.");
        }
        return statistics;
    }
}