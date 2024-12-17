
package org.example.course_server.controller;

import org.example.course_server.entity.Booking;
import org.example.course_server.entity.Transaction;
import org.example.course_server.entity.User;
import org.example.course_server.entity.ParkingSpot;
import org.example.course_server.repository.BookingRepo;
import org.example.course_server.service.BookingService;
import org.example.course_server.service.JwtService;
import org.example.course_server.service.UserService;
import org.example.course_server.service.ParkingSpotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final BookingRepo bookingRepo;
    private final UserService userService;
    private final ParkingSpotService parkingSpotService;
    private final JwtService jwtService;

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

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.findAll();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getBookingsByUser(@PathVariable Long userId) {
        Optional<User> userOptional = userService.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(List.of());
        }

        List<Booking> bookings = bookingService.findByUser(userOptional.get());
        return ResponseEntity.ok(bookings);
    }

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

    @GetMapping("/today")
    public ResponseEntity<List<Booking>> getTodayBookings() {
        List<Booking> todayBookings = bookingRepo.findTodayBookings();
        return ResponseEntity.ok(todayBookings);
    }

}
