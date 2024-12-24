package org.example.course_server.repository;

import org.example.course_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

/**
 * Репозиторий для работы с сущностью пользователя.
 * Предоставляет методы для выполнения операций с данными пользователей.
 */
public interface UserRepo extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по имени.
     *
     * @param username Имя пользователя.
     * @return Опционально пользователь с таким именем.
     */
    Optional<User> findByUsername(String username);

    /**
     * Находит пользователя по email.
     *
     * @param email Email пользователя.
     * @return Опционально пользователь с таким email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Находит пользователей, чьи имена содержат указанный текст.
     *
     * @param username Текст для поиска в имени пользователя.
     * @return Список пользователей, чьи имена содержат указанный текст.
     */
    List<User> findByUsernameContaining(String username);
}