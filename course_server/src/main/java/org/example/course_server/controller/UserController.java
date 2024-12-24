package org.example.course_server.controller;

import org.example.course_server.entity.*;
import org.example.course_server.repository.UserRepo;
import org.example.course_server.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

/**
 * Контроллер для управления пользователями.
 * Предоставляет API для регистрации, авторизации, поиска, получения и удаления пользователей.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserController(UserService userService, UserRepo userRepo, JwtService jwtService) {
        this.userService = userService;
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param user пользователь, которого необходимо зарегистрировать.
     * @return {@link ResponseEntity} с данными созданного пользователя.
     */
    @PostMapping
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User createdUser = userService.registerUser(user);
        return ResponseEntity.ok(createdUser);
    }

    /**
     * Возвращает список всех пользователей.
     *
     * @return {@link ResponseEntity} со списком пользователей.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    /**
     * Возвращает пользователя по его идентификатору.
     *
     * @param id ID пользователя.
     * @return {@link ResponseEntity} с данными пользователя или статус 404, если пользователь не найден.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param id ID пользователя.
     * @return {@link ResponseEntity} со статусом 204 (No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userRepo.deleteById(id); // Реализация удаления в UserService
        return ResponseEntity.noContent().build();
    }

    /**
     * Выполняет поиск пользователей по части имени.
     *
     * @param username часть имени пользователя для поиска.
     * @return {@link ResponseEntity} со списком подходящих пользователей.
     */
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String username) {
        List<User> users = userRepo.findByUsernameContaining(username);
        return ResponseEntity.ok(users);
    }

    /**
     * Регистрирует нового пользователя с предоставлением имени, email и пароля.
     * Проверяет, не существует ли пользователь с таким именем.
     *
     * @param userData данные пользователя (username, email, password).
     * @return {@link ResponseEntity} с сообщением об успешной регистрации или ошибке.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> userData) {
        String username = userData.get("username");
        String email = userData.get("email");
        String password = userData.get("password");

        if (userService.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("Пользователь с таким именем уже существует.");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));  // Хешируем пароль
        user.setUserType(UserType.CUSTOMER);  // По умолчанию обычный пользователь

        userRepo.save(user);

        return ResponseEntity.ok("Пользователь зарегистрирован.");
    }

    /**
     * Авторизует пользователя с проверкой имени и пароля.
     * Генерирует JWT-токен для успешной авторизации.
     *
     * @param loginData данные для входа (username, password).
     * @return {@link ResponseEntity} с JWT-токеном и информацией о пользователе, или сообщение об ошибке.
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isEmpty() || !userService.checkPassword(password, userOptional.get())) {
            return ResponseEntity.status(401).body("Неверные учетные данные.");
        }

        User user = userOptional.get();
        String token = jwtService.generateToken(user.getUsername(), user.getUserType().name());
        return ResponseEntity.ok(Map.of("token", token, "id", user.getId(), "userType", user.getUserType()));
    }
}