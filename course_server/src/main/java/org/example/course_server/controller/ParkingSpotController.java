package org.example.course_server.controller;

import org.example.course_server.entity.Booking;
import org.example.course_server.entity.ParkingSpot;
import org.example.course_server.entity.User;
import org.example.course_server.repository.BookingRepo;
import org.example.course_server.repository.ParkingSpotRepo;
import org.example.course_server.service.ParkingSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/parking-spots")
public class ParkingSpotController {

    @Autowired
    private ParkingSpotService parkingSpotService;

    @Autowired
    private ParkingSpotRepo parkingSpotRepo;

    @Autowired
    private BookingRepo bookingRepo;


    @PostMapping
    public ResponseEntity<ParkingSpot> registerParkingSpot(@RequestBody ParkingSpot parkingSpot) {
        ParkingSpot createdParkingSpot = parkingSpotService.registerParkingSpot(parkingSpot);
        return ResponseEntity.ok(createdParkingSpot);
    }

    @GetMapping
    public ResponseEntity<List<ParkingSpot>> getAllParkingSpots() {
        List<ParkingSpot> spots = parkingSpotService.findAll();
        return ResponseEntity.ok(spots);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpot> getParkingSpotById(@PathVariable Long id) {
        Optional<ParkingSpot> spot = parkingSpotService.findById(id);
        return spot.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/parking-statistics")
    public ResponseEntity<Map<String, int[]>> getParkingStatistics() {
        List<ParkingSpot> spots = parkingSpotRepo.findAll();
        List<Booking> bookings = bookingRepo.findAll();

        Map<String, int[]> statistics = new HashMap<>();

        for (ParkingSpot spot : spots) {
            String tcName = spot.getTcName();
            statistics.putIfAbsent(tcName, new int[]{0, 0}); // [занятые места, всего мест]
            statistics.get(tcName)[1]++; // Всего мест увеличиваем на 1

            // Проверяем бронирования, чтобы найти занятые места
            for (Booking booking : bookings) {
                if (booking.getTcName().equals(tcName) &&
                        booking.getSpotNumber() == spot.getSpotNumber() &&
                        !"COMPLETED".equals(booking.getStatus())) {
                    statistics.get(tcName)[0]++; // Увеличиваем занятые места
                    break;
                }
            }
        }

        return ResponseEntity.ok(statistics);
    }
}

