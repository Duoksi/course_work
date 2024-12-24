package org.example.course_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Сущность для транзакции.
 * Хранит информацию о транзакции, связанную с бронированием и пользователем.
 */
@Getter
@Setter
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Бронирование, с которым связана транзакция.
     */
    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = true)
    private Booking booking;

    /**
     * Пользователь, который совершил транзакцию.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Сумма транзакции.
     */
    private int amount;

    /**
     * Время совершения транзакции.
     */
    @Column(columnDefinition = "TIMESTAMP(0)")
    @CreationTimestamp
    private LocalDateTime transactionTime;

    /**
     * Статус транзакции.
     */
    private String status;
}