package org.example.course_server.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурационный класс для настройки безопасности приложения.
 * Использует Spring Security для определения политик доступа.
 */
@Configuration
public class SecurityConfig {

    /**
     * Определяет цепочку фильтров безопасности для обработки HTTP-запросов.
     *
     * <p>Настройки:
     * <ul>
     *     <li>Отключение CSRF-защиты.</li>
     *     <li>Разрешение доступа ко всем запросам без аутентификации.</li>
     * </ul>
     *
     * @param http объект {@link HttpSecurity}, предоставляющий методы настройки безопасности.
     * @return экземпляр {@link SecurityFilterChain}, содержащий определённую конфигурацию безопасности.
     * @throws Exception если возникает ошибка при настройке {@link HttpSecurity}.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests().anyRequest().permitAll();

        return http.build();
    }
}