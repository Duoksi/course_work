
package org.example.course_server.controller;

import org.example.course_server.entity.Booking;
import org.example.course_server.entity.User;
import org.example.course_server.entity.ParkingSpot;
import org.example.course_server.service.BookingService;
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
    private final UserService userService;
    private final ParkingSpotService parkingSpotService;

    public BookingController(
            BookingService bookingService,
            UserService userService,
            ParkingSpotService parkingSpotService) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.parkingSpotService = parkingSpotService;
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Map<String, Object> bookingData) {
        Long userId = ((Number) bookingData.get("userId")).longValue();
        int spotNumber = (int) bookingData.get("spotNumber");
        String tcName = (String) bookingData.get("tcName");
        String startTime = (String) bookingData.get("startTime");
        String endTime = (String) bookingData.get("endTime");

        // 1. Получаем пользователя по ID
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

        System.out.printf("Бронирование: User %d, Spot %d, TC: %s, Время: %s - %s%n",
                userId, spotNumber, tcName, startTime, endTime);
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
}
