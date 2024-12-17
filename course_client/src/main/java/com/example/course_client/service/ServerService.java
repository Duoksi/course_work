package com.example.course_client.service;

import java.net.URI;
import java.net.http.*;
import java.util.*;

import com.example.course_client.dto.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import javafx.scene.control.Alert;
import org.json.JSONObject;

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

    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
