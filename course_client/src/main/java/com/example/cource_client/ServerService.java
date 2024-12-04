package com.example.cource_client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerService {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public Long sendBookingRequest(int spotNumber, String tcName, String startDateTime, String endDateTime) {
        try {
            String jsonBody = String.format(
                    "{\"userId\": 1, \"spotNumber\": %d, \"tcName\": \"%s\", \"startTime\": \"%s\", \"endTime\": \"%s\"}",
                    spotNumber, tcName, startDateTime, endDateTime
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/bookings"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Извлекаем bookingId из ответа
                String responseBody = response.body();
                Long bookingId = Long.valueOf(responseBody.replaceAll("[^0-9]", ""));
                System.out.println("Бронирование создано с ID: " + bookingId);
                return bookingId;
            } else {
                System.out.println("Ошибка бронирования: " + response.body());
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
                Long transactionId = Long.valueOf(responseBody.replaceAll("[^0-9]", ""));
                System.out.println("Транзакция создана с ID: " + transactionId);
                return transactionId;
            } else {
                System.out.println("Ошибка создания транзакции: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateTransactionStatus(Long transactionId, String newStatus) {
        try {
            String jsonBody = String.format(
                    "{\"newStatus\": \"%s\"}", newStatus
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/transactions/update-status?transactionId=" + transactionId + "&newStatus=" + newStatus))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("Статус транзакции обновлён на " + newStatus);
            } else {
                System.out.println("Ошибка обновления статуса транзакции: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                System.out.println("Booking ID обновлён для транзакции " + transactionId);
            } else {
                System.out.println("Ошибка обновления Booking ID: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
