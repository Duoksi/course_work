package org.example.course_server.service;

import org.example.course_server.entity.*;
import org.example.course_server.repository.BookingRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Сервис для работы с сущностью бронирования.
 * Предоставляет методы для создания и поиска бронирований.
 */
@Service
public class BookingService {

    private final BookingRepo bookingRepo;

    public BookingService(BookingRepo bookingRepo) {
        this.bookingRepo = bookingRepo;
    }

    /**
     * Создаёт новое бронирование для пользователя.
     *
     * @param user      Пользователь, создающий бронирование.
     * @param spotNumber Номер парковочного места.
     * @param tcName     Название торгового центра.
     * @param startTime  Время начала бронирования.
     * @param endTime    Время окончания бронирования.
     * @return Сохранённое бронирование.
     */
    public Booking createBooking(User user, int spotNumber, String tcName, LocalDateTime startTime, LocalDateTime endTime) {
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setSpotNumber(spotNumber);
        booking.setTcName(tcName);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setStatus("RESERVED");
        return bookingRepo.save(booking);
    }

    /**
     * Находит все бронирования.
     *
     * @return Список всех бронирований.
     */
    public List<Booking> findAll() {
        return bookingRepo.findAll();
    }

    /**
     * Находит бронирование по ID.
     *
     * @param id ID бронирования.
     * @return Опционально найденное бронирование.
     */
    public Optional<Booking> findById(Long id) {
        return bookingRepo.findById(id);
    }

    /**
     * Находит все бронирования для указанного пользователя.
     *
     * @param user Пользователь, чьи бронирования нужно найти.
     * @return Список бронирований пользователя.
     */
    public List<Booking> findByUser(User user) {
        return bookingRepo.findByUser(user);
    }
}