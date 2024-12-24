package com.example.course_client.dto;

/**
 * DTO для представления информации о пользователе.
 */
public class UserDTO {
    /**
     * Уникальный идентификатор пользователя.
     */
    private Long id;

    /**
     * Имя пользователя.
     */
    private String username;

    /**
     * Пароль пользователя.
     */
    private String password;

    /**
     * Электронная почта пользователя.
     */
    private String email;

    /**
     * Тип пользователя (например, ADMIN, CUSTOMER).
     */
    private String userType;

    /**
     * Конструктор по умолчанию.
     */
    public UserDTO() {}

    /**
     * Конструктор с параметрами.
     *
     * @param username  Имя пользователя.
     * @param email     Электронная почта пользователя.
     * @param password  Пароль пользователя.
     * @param userType  Тип пользователя.
     */
    public UserDTO(String username, String email, String password, String userType) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}