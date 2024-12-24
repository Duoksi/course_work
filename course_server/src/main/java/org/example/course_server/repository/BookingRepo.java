package org.example.course_server.repository;

import org.example.course_server.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий для работы с сущностью бронирования.
 * Предоставляет методы для выполнения операций с данными бронирований.
 */
public interface BookingRepo extends JpaRepository<Booking, Long> {

    /**
     * Находит все бронирования для указанного пользователя.
     *
     * @param user Пользователь, чьи бронирования нужно найти.
     * @return Список бронирований.
     */
    List<Booking> findByUser(User user);

    /**
     * Проверяет, занято ли парковочное место в определённый момент времени.
     *
     * @param spotNumber Номер парковочного места.
     * @param tcName     Название торгового центра.
     * @param currentTime Время, для которого проверяется доступность.
     * @return true, если место занято, иначе false.
     */
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.spotNumber = :spotNumber AND b.tcName = :tcName AND :currentTime BETWEEN b.startTime AND b.endTime")
    boolean isParkingSpotOccupied(@Param("spotNumber") int spotNumber, @Param("tcName") String tcName, @Param("currentTime") LocalDateTime currentTime);

    /**
     * Находит все бронирования, сделанные на текущий день.
     *
     * @return Список бронирований на текущий день.
     */
    @Query("SELECT b FROM Booking b WHERE DATE(b.startTime) = CURRENT_DATE")
    List<Booking> findTodayBookings();
}