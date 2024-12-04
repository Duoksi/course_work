package org.example.course_server.repository;

import org.example.course_server.entity.Booking;
import org.example.course_server.entity.User;
import org.example.course_server.entity.ParkingSpot;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepo extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.spotNumber = :spotNumber AND b.tcName = :tcName AND :currentTime BETWEEN b.startTime AND b.endTime")
    boolean isParkingSpotOccupied(@Param("spotNumber") int spotNumber, @Param("tcName") String tcName, @Param("currentTime") LocalDateTime currentTime);
}
