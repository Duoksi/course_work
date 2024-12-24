package org.example.course_server.service;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Сервис для работы с JWT (JSON Web Tokens).
 * Предоставляет методы для генерации и проверки токенов.
 */
@Service
public class JwtService {

    // Секретный ключ для подписи токенов
    private final String SECRET_KEY = "mySuperSecureKeyWithMoreThan32Characters123!";

    /**
     * Генерация токена для пользователя с указанием его роли.
     *
     * @param username Имя пользователя.
     * @param role     Роль пользователя.
     * @return Сгенерированный токен.
     */
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 день
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * Проверка валидности токена.
     *
     * @param token Токен для проверки.
     * @return true, если токен валиден, иначе false.
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Извлечение всех данных (claims) из токена.
     *
     * @param token Токен.
     * @return Claims, содержащие информацию о токене.
     */
    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Некорректный токен");
        }
    }
}