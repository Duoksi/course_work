package org.example.course_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Главный класс для запуска Spring Boot приложения.
 * Включает поддержку планирования задач с помощью аннотации {@link EnableScheduling}.
 */
@SpringBootApplication
@EnableScheduling
public class CourseServerApplication {

    /**
     * Главный метод для запуска Spring Boot приложения.
     * Инициализирует приложение с помощью {@link SpringApplication}.
     *
     * @param args Аргументы командной строки.
     */
    public static void main(String[] args) {
        SpringApplication.run(CourseServerApplication.class, args);
    }
}