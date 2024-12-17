package org.example.course_server.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // Секретный ключ для подписи токенов (замени на свой)
    private final String SECRET_KEY = "mySuperSecureKeyWithMoreThan32Characters123!";

    // Генерация токена
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)                         // Устанавливаем имя пользователя
                .claim("role", role)                           // Добавляем роль
                .setIssuedAt(new Date())                       // Время выпуска токена
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // Срок действия 1 день
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)  // Подпись токена
                .compact();                                    // Завершение сборки токена
    }

    // Проверка валидности токена
    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);            // Извлекаем все утверждения
            return !claims.getExpiration().before(new Date());  // Проверяем, не истёк ли срок действия
        } catch (Exception e) {
            return false;                                       // Токен недействителен
        }
    }

    // Извлечение имени пользователя из токена
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();            // Извлекаем subject (username)
    }

    // Извлечение всех данных (Claims) из токена
    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)  // Тот же ключ, что при генерации
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            e.printStackTrace(); // Логи помогут увидеть, в чём ошибка
            throw new IllegalArgumentException("Некорректный токен");
        }
    }

    // Генерация ключа на основе строки
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());       // Преобразуем секретный ключ в формат HMAC
    }
}