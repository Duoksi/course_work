package com.example.course_client.dto;

/**
 * DTO для представления информации о парковочном месте.
 */
public class ParkingSpotDTO {
    /**
     * Уникальный идентификатор парковочного места.
     */
    private Long id;

    /**
     * Номер парковочного места.
     */
    private int spotNumber;

    /**
     * Название торгового центра, к которому относится место.
     */
    private String tcName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}