package org.example.course_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Сущность для парковочного места в торговом центре (ТЦ).
 * Хранит информацию о парковочных местах и их номерах.
 */
@Getter
@Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"spotNumber", "tcName"}))
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Внутренний уникальный ID для сущности

    /**
     * Номер парковочного места.
     */
    private int spotNumber;

    /**
     * Название торгового центра (ТЦ), где находится парковочное место.
     */
    private String tcName;
}