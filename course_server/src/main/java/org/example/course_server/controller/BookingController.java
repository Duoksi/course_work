package org.example.course_server.controller;

import org.example.course_server.entity.*;
import org.example.course_server.repository.BookingRepo;
import org.example.course_server.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Контроллер для работы с сущностью бронирования.
 * Обрабатывает запросы, связанные с созданием, просмотром и обновлением бронирований.
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final BookingRepo bookingRepo;
    private final UserService userService;
    private final ParkingSpotService parkingSpotService;
    private final JwtService jwtService;

    /**
     * Конструктор для внедрения зависимостей.
     *
     * @param bookingService     сервис для работы с бронированиями.
     * @param bookingRepo        репозиторий для работы с хранилищем бронирований.
     * @param userService        сервис для работы с пользователями.
     * @param parkingSpotService сервис для работы с парковочными местами.
     * @param jwtService         сервис для работы с JWT токенами.
     */
    public BookingController(
            BookingService bookingService,
            BookingRepo bookingRepo,
            UserService userService,
            ParkingSpotService parkingSpotService,
            JwtService jwtService) {
        this.bookingService = bookingService;
        this.bookingRepo = bookingRepo;
        this.userService = userService;
        this.parkingSpotService = parkingSpotService;
        this.jwtService = jwtService;
    }

    /**
     * Создаёт новое бронирование.
     *
     * @param token       JWT токен для авторизации.
     * @param bookingData данные бронирования (userId, spotNumber, tcName, startTime, endTime).
     * @return {@link ResponseEntity} с созданным бронированием или сообщением об ошибке.
     */
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestHeader("Authorization") String token,
                                            @RequestBody Map<String, Object> bookingData) {
        Long userId = ((Number) bookingData.get("userId")).longValue();
        int spotNumber = (int) bookingData.get("spotNumber");
        String tcName = (String) bookingData.get("tcName");
        String startTime = (String) bookingData.get("startTime");
        String endTime = (String) bookingData.get("endTime");

        token = token.replace("Bearer ", "");
        if (!jwtService.validateToken(token)) {
            return ResponseEntity.status(401).body("Неверный или истёкший токен.");
        }

        Optional<User> userOptional = userService.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found with id: " + userId);
        }

        // 3. Проверяем занятость места
        boolean isOccupied = parkingSpotService.isSpotOccupied(spotNumber, tcName);
        if (isOccupied) {
            return ResponseEntity.badRequest().body("Parking spot is already occupied.");
        }


        // 4. Создаём бронирование
        Booking booking = bookingService.createBooking(
                userOptional.get(),
                spotNumber,
                tcName,
                LocalDateTime.parse(startTime),
                LocalDateTime.parse(endTime)
        );

        return ResponseEntity.ok(booking);
    }

    /**
     * Получает список всех бронирований.
     *
     * @return {@link ResponseEntity} с перечнем бронирований.
     */
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.findAll();
        return ResponseEntity.ok(bookings);
    }

    /**
     * Получает список бронирований конкретного пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return {@link ResponseEntity} с перечнем бронирований или пустым списком.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getBookingsByUser(@PathVariable Long userId) {
        Optional<User> userOptional = userService.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(List.of());
        }

        List<Booking> bookings = bookingService.findByUser(userOptional.get());
        return ResponseEntity.ok(bookings);
    }

    /**
     * Обновляет статус бронирования.
     *
     * @param id   идентификатор бронирования.
     * @param body тело запроса с новым статусом.
     * @return {@link ResponseEntity} с подтверждением обновления или сообщением об ошибке.
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateBookingStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String newStatus = body.get("newStatus");

        Optional<Booking> optionalBooking = bookingService.findById(id);

        if (optionalBooking.isEmpty()) {
            return ResponseEntity.badRequest().body("Бронирование не найдено.");
        }

        Booking booking = optionalBooking.get();
        booking.setStatus(newStatus);
        bookingRepo.save(booking); // Сохраняем изменения

        return ResponseEntity.ok("Статус бронирования обновлён на " + newStatus);
    }

    /**
     * Обновляет статусы всех актуальных бронирований.
     *
     * <p>Статусы:
     * <ul>
     *     <li>RESERVED — если бронирование ещё не началось.</li>
     *     <li>IN_PROGRESS — если бронирование активно.</li>
     *     <li>COMPLETED — если бронирование завершено.</li>
     * </ul>
     *
     * @return {@link ResponseEntity} с подтверждением обновления статусов.
     */
    @PutMapping("/update-statuses")
    public ResponseEntity<?> updateBookingStatuses() {
        List<Booking> bookings = bookingRepo.findTodayBookings();
        LocalDateTime now = LocalDateTime.now();

        for (Booking booking : bookings) {
            if (now.isBefore(booking.getStartTime())) {
                booking.setStatus("RESERVED");
            } else if (now.isAfter(booking.getStartTime()) && now.isBefore(booking.getEndTime())) {
                booking.setStatus("IN_PROGRESS");
            } else if (now.isAfter(booking.getEndTime())) {
                booking.setStatus("COMPLETED");
            }
            bookingRepo.save(booking);
        }
        return ResponseEntity.ok("Statuses updated.");
    }

    /**
     * Получает список бронирований на текущий день.
     *
     * @return {@link ResponseEntity} с перечнем бронирований за сегодня.
     */
    @GetMapping("/today")
    public ResponseEntity<List<Booking>> getTodayBookings() {
        List<Booking> todayBookings = bookingRepo.findTodayBookings();
        return ResponseEntity.ok(todayBookings);
    }
}