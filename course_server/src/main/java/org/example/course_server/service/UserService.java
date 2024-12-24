package org.example.course_server.service;

import org.example.course_server.entity.*;
import org.example.course_server.repository.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Сервис для работы с сущностью пользователя.
 * Предоставляет методы для регистрации пользователей и управления их данными.
 */
@Service
public class UserService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = new BCryptPasswordEncoder(); // Инициализация
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param user Пользователь.
     * @return Сохранённый пользователь.
     */
    public User registerUser(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        if (user.getUserType() == null) {
            user.setUserType(UserType.CUSTOMER);  // Устанавливаем тип по умолчанию
        }
        return userRepo.save(user);
    }

    /**
     * Находит всех пользователей.
     *
     * @return Список всех пользователей.
     */
    public List<User> findAll() {
        return userRepo.findAll();
    }

    /**
     * Находит пользователя по имени.
     *
     * @param username Имя пользователя.
     * @return Опционально найденный пользователь.
     */
    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    /**
     * Находит пользователя по email.
     *
     * @param email Email пользователя.
     * @return Опционально найденный пользователь.
     */
    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    /**
     * Находит пользователя по ID.
     *
     * @param id ID пользователя.
     * @return Опционально найденный пользователь.
     */
    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    /**
     * Проверяет, совпадает ли введённый пароль с сохранённым.
     *
     * @param rawPassword Введённый пароль.
     * @param user        Пользователь.
     * @return true, если пароли совпадают, иначе false.
     */
    public boolean checkPassword(String rawPassword, User user) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    /**
     * Проверяет, существует ли пользователь с таким именем.
     *
     * @param username Имя пользователя.
     * @return true, если пользователь существует, иначе false.
     */
    public boolean existsByUsername(String username) {
        return userRepo.findByUsername(username).isPresent();
    }
}