package com.example.course_client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDTO {
    private Long id;
    private Long bookingId;
    private Long userId;
    private int amount;
    private String transactionTime;
    private String status;

    @JsonProperty("user") // Обработка вложенного объекта
    public void unpackUserId(Map<String, Object> user) {
        if (user != null && user.containsKey("id")) {
            this.userId = ((Number) user.get("id")).longValue();
        }
    }

    @JsonProperty("booking") // Обработка вложенного объекта
    public void unpackBookingId(Map<String, Object> booking) {
        if (booking != null && booking.containsKey("id")) {
            this.bookingId = ((Number) booking.get("id")).longValue();
        }
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
