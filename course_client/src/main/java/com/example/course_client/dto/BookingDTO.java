package com.example.course_client.dto;

import com.fasterxml.jackson.annotation.*;

import java.util.Map;

/**
 * DTO для представления информации о бронировании.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingDTO {
    /**
     * Уникальный идентификатор бронирования.
     */
    private Long id;

    /**
     * Идентификатор пользователя, связанного с бронированием.
     */
    private Long userId;

    /**
     * Номер парковочного места.
     */
    private int spotNumber;

    /**
     * Название торгового центра.
     */
    private String tcName;

    /**
     * Время создания бронирования.
     */
    private String bookingTime;

    /**
     * Время начала бронирования.
     */
    private String startTime;

    /**
     * Время окончания бронирования.
     */
    private String endTime;

    /**
     * Статус бронирования.
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getSpotNumber() {
        return spotNumber;
    }

    public void setSpotNumber(int spotNumber) {
        this.spotNumber = spotNumber;
    }

    public String getTcName() {
        return tcName;
    }

    public void setTcName(String tcName) {
        this.tcName = tcName;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}