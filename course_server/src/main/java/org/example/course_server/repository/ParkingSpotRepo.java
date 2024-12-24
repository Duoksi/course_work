package org.example.course_server.repository;

import org.example.course_server.entity.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с сущностью парковочного места.
 * Предоставляет методы для выполнения операций с данными парковочных мест.
 */
public interface ParkingSpotRepo extends JpaRepository<ParkingSpot, Long> {
}