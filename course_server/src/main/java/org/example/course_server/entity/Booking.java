package org.example.course_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Сущность для бронирования парковочного места.
 * Хранит информацию о бронировании, пользователе и времени.
 */
@Getter
@Setter
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Пользователь, который сделал бронирование.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Номер парковочного места.
     */
    private int spotNumber;

    /**
     * Название торгового центра (ТЦ), где находится парковочное место.
     */
    private String tcName;

    /**
     * Время создания бронирования.
     */
    @Column(columnDefinition = "TIMESTAMP(0)")
    @CreationTimestamp
    private LocalDateTime bookingTime;

    /**
     * Время начала бронирования.
     */
    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime startTime;

    /**
     * Время окончания бронирования.
     */
    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime endTime;

    /**
     * Статус бронирования.
     */
    private String status;
}