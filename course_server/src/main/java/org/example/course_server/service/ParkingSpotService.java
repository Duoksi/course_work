package org.example.course_server.service;

import org.example.course_server.entity.*;
import org.example.course_server.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Сервис для работы с сущностью парковочного места.
 * Предоставляет методы для регистрации, проверки занятости парковочных мест и поиска.
 */
@Service
public class ParkingSpotService {

    private final ParkingSpotRepo parkingSpotRepo;
    private final BookingRepo bookingRepo;

    public ParkingSpotService(ParkingSpotRepo parkingSpotRepo, BookingRepo bookingRepo) {
        this.parkingSpotRepo = parkingSpotRepo;
        this.bookingRepo = bookingRepo;
    }

    /**
     * Находит все парковочные места.
     *
     * @return Список всех парковочных мест.
     */
    public List<ParkingSpot> findAll() {
        return parkingSpotRepo.findAll();
    }

    /**
     * Проверяет, занято ли парковочное место в указанное время.
     *
     * @param spotNumber Номер парковочного места.
     * @param tcName     Название торгового центра.
     * @return true, если место занято, иначе false.
     */
    public boolean isSpotOccupied(int spotNumber, String tcName) {
        return bookingRepo.isParkingSpotOccupied(spotNumber, tcName, LocalDateTime.now());
    }

    /**
     * Находит парковочное место по ID.
     *
     * @param id ID парковочного места.
     * @return Опционально найденное парковочное место.
     */
    public Optional<ParkingSpot> findById(Long id) {
        return parkingSpotRepo.findById(id);
    }

    /**
     * Регистрирует новое парковочное место.
     *
     * @param parkingSpot Парковочное место.
     * @return Сохранённое парковочное место.
     */
    public ParkingSpot registerParkingSpot(ParkingSpot parkingSpot) {
        return parkingSpotRepo.save(parkingSpot);
    }
}