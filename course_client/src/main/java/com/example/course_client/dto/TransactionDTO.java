package com.example.course_client.dto;

import com.fasterxml.jackson.annotation.*;

import java.util.Map;

/**
 * DTO для представления информации о транзакции.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDTO {
    /**
     * Уникальный идентификатор транзакции.
     */
    private Long id;

    /**
     * Идентификатор бронирования, связанного с транзакцией.
     */
    private Long bookingId;

    /**
     * Идентификатор пользователя, связанного с транзакцией.
     */
    private Long userId;

    /**
     * Сумма транзакции.
     */
    private int amount;

    /**
     * Время создания транзакции.
     */
    private String transactionTime;

    /**
     * Статус транзакции.
     */
    private String status;

    /**
     * Обрабатывает вложенный объект пользователя для извлечения идентификатора.
     *
     * @param user объект пользователя.
     */
    @JsonProperty("user")
    public void unpackUserId(Map<String, Object> user) {
        if (user != null && user.containsKey("id")) {
            this.userId = ((Number) user.get("id")).longValue();
        }
    }

    /**
     * Обрабатывает вложенный объект бронирования для извлечения идентификатора.
     *
     * @param booking объект бронирования.
     */
    @JsonProperty("booking")
    public void unpackBookingId(Map<String, Object> booking) {
        if (booking != null && booking.containsKey("id")) {
            this.bookingId = ((Number) booking.get("id")).longValue();
        }
    }

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