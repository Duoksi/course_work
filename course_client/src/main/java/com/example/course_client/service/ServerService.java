package com.example.course_client.service;

import java.net.URI;
import java.net.http.*;
import java.util.*;

import com.example.course_client.dto.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import javafx.scene.control.Alert;
import org.json.JSONObject;

/**
 * Класс для взаимодействия с сервером через HTTP.
 */
public class ServerService {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private String token;
    private Long id;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Отправляет запрос на бронирование парковочного места.
     *
     * @param userId       ID пользователя.
     * @param spotNumber   Номер парковочного места.
     * @param tcName       Название торгового центра.
     * @param startDateTime Дата и время начала бронирования.
     * @param endDateTime  Дата и время окончания бронирования.
     * @return ID созданного бронирования или {@code null}, если запрос не удался.
     */
    public Long sendBookingRequest(Long userId, int spotNumber, String tcName, String startDateTime, String endDateTime) {
        try {
            String jsonBody = String.format(
                    "{\"userId\": %d, \"spotNumber\": %d, \"tcName\": \"%s\", \"startTime\": \"%s\", \"endTime\": \"%s\"}",
                    userId, spotNumber, tcName, startDateTime, endDateTime
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/bookings"))
                    .header("Authorization", "Bearer " + getToken())  // Используем токен
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Извлечение bookingId из успешного ответа
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> responseMap = objectMapper.readValue(response.body(), Map.class);
                return Long.valueOf(responseMap.get("id").toString());  // Вернём ID бронирования
            } else {
                showError("Ошибка бронирования: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Создаёт новую транзакцию.
     *
     * @param userId ID пользователя.
     * @param amount Сумма транзакции.
     * @return ID созданной транзакции или {@code null}, если запрос не удался.
     */
    public Long createTransaction(Long userId, int amount) {
        try {
            String jsonBody = String.format(
                    "{\"userId\": %d, \"amount\": %d}",
                    userId, amount
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/transactions"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Извлекаем ID транзакции из ответа (предполагается, что возвращается JSON с "id")
                String responseBody = response.body();
                JSONObject jsonResponse = new JSONObject(responseBody);
                Long transactionId = ((Number) jsonResponse.get("id")).longValue();
                return transactionId;
            } else {
                showError("Ошибка создания транзакции: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Обновляет статус транзакции.
     *
     * @param transactionId ID транзакции.
     * @param newStatus     Новый статус транзакции.
     * @return {@code true}, если запрос выполнен успешно; иначе {@code false}.
     */
    public boolean updateTransactionStatus(Long transactionId, String newStatus) {
        try {
            Map<String, String> body = Map.of("newStatus", newStatus);
            String json = objectMapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/transactions/" + transactionId + "/status"))
                    .header("Authorization", "Bearer " + getToken())
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Обновляет ID бронирования, связанного с транзакцией.
     *
     * @param transactionId ID транзакции.
     * @param bookingId     Новый ID бронирования.
     */
    public void updateBookingId(Long transactionId, Long bookingId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/transactions/update-booking?transactionId=" + transactionId + "&bookingId=" + bookingId))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
            } else {
                showError("Ошибка обновления Booking ID: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Выполняет вход в систему с указанными учетными данными.
     *
     * @param username Имя пользователя.
     * @param password Пароль.
     * @return Тип пользователя, если вход успешен, или {@code null}, если произошла ошибка.
     */
    public String login(String username, String password) {
        try {
            String jsonBody = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/users/login"))  // Путь к логину
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Используем ObjectMapper для парсинга JSON ответа
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(response.body());
                this.token = root.get("token").asText();  // Сохраняем токен
                this.id = root.get("id").asLong();
                System.out.println("Токен получен: " + getToken());
                return root.get("userType").asText();
            } else {
                showError("Ошибка логина");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param username Имя пользователя.
     * @param email    Электронная почта.
     * @param password Пароль.
     * @return {@code true}, если регистрация успешна; иначе {@code false}.
     */
    public boolean register(String username, String email,String password) {
        try {
            String jsonBody = String.format("{\"username\":\"%s\", \"email\":\"%s\", \"password\":\"%s\"}", username, email, password);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/users/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;  // Успех при статусе 200
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Создаёт нового пользователя с указанными данными.
     *
     * @param user DTO пользователя с необходимыми данными.
     * @return {@code true}, если пользователь успешно создан; иначе {@code false}.
     */
    public boolean createUser(UserDTO user) {
        try {
            String json = objectMapper.writeValueAsString(user);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/users"))
                    .header("Authorization", "Bearer " + getToken())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Удаляет пользователя по его ID.
     *
     * @param userId ID пользователя.
     * @return {@code true}, если удаление прошло успешно; иначе {@code false}.
     */
    public boolean deleteUser(Long userId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/users/" + userId))
                    .header("Authorization", "Bearer " + getToken())
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 204;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Обновляет статус бронирования.
     *
     * @param bookingId ID бронирования.
     * @param status    Новый статус.
     * @return {@code true}, если обновление прошло успешно; иначе {@code false}.
     */
    public boolean updateBookingStatus(Long bookingId, String status) {
        try {
            Map<String, String> body = Map.of("newStatus", status);
            String json = objectMapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/bookings/" + bookingId + "/status"))
                    .header("Authorization", "Bearer " + getToken())
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Ищет пользователей по имени.
     *
     * @param username Имя пользователя.
     * @return Список пользователей, соответствующих имени, или пустой список, если ничего не найдено.
     */
    public List<UserDTO> findUserByUsername(String username) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/users/search?username=" + username))
                    .header("Authorization", "Bearer " + getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), new TypeReference<List<UserDTO>>() {});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * Ищет бронирования, связанные с указанным ID пользователя.
     *
     * @param userId ID пользователя.
     * @return Список бронирований или пустой список, если ничего не найдено.
     */
    public List<BookingDTO> findBookingsByUserId(Long userId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/bookings/user/" + userId))
                    .header("Authorization", "Bearer " + getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), new TypeReference<List<BookingDTO>>() {});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * Ищет транзакции, связанные с указанным ID пользователя.
     *
     * @param userId ID пользователя.
     * @return Список транзакций или пустой список, если ничего не найдено.
     */
    public List<TransactionDTO> findTransactionsByUserId(Long userId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/transactions/user/" + userId))
                    .header("Authorization", "Bearer " + getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), new TypeReference<List<TransactionDTO>>() {});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * Получает статус парковочного места в указанном ТЦ.
     *
     * @param tcName     Название торгового центра.
     * @param spotNumber Номер парковочного места.
     * @return Текущий статус парковочного места, или "AVAILABLE", если оно свободно.
     */
    public String getSpotStatus(String tcName, int spotNumber) {
        List<BookingDTO> bookings = fetchTodayBookings();

        // Ищем бронирование для указанного места, ТЦ и не завершённое
        for (BookingDTO booking : bookings) {
            if (booking.getTcName().equals(tcName) &&
                    booking.getSpotNumber() == spotNumber &&
                    !"COMPLETED".equals(booking.getStatus())) {
                return booking.getStatus(); // Возвращаем текущий статус
            }
        }

        // Если бронирование не найдено, место свободно
        return "AVAILABLE";
    }

    /**
     * Получает список бронирований на текущий день.
     *
     * @return Список бронирований или пустой список, если ничего не найдено.
     */
    public List<BookingDTO> fetchTodayBookings() {
        try {
            // Сначала обновляем статусы на сервере
            HttpRequest updateRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/bookings/update-statuses"))
                    .header("Authorization", "Bearer " + token)
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();
            httpClient.send(updateRequest, HttpResponse.BodyHandlers.discarding());

            // Затем получаем обновлённые бронирования
            HttpRequest fetchRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/bookings/today"))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(fetchRequest, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), new TypeReference<>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Отображает сообщение об ошибке в графическом интерфейсе.
     *
     * @param message Текст сообщения об ошибке.
     */
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}