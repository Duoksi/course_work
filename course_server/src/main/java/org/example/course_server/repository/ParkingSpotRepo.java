package org.example.course_server.repository;

import org.example.course_server.entity.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingSpotRepo extends JpaRepository<ParkingSpot, Long> {
}