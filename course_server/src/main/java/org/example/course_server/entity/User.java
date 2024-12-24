package org.example.course_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Сущность для пользователя системы.
 * Хранит информацию о пользователе, включая имя, email и тип пользователя.
 */
@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя пользователя.
     */
    private String username;

    /**
     * Email пользователя.
     */
    private String email;

    /**
     * Пароль пользователя.
     */
    private String password;

    /**
     * Тип пользователя (администратор или обычный пользователь).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType = UserType.CUSTOMER;
}