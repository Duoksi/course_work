package org.example.course_server.controller;

import org.example.course_server.entity.ParkingSpot;
import org.example.course_server.entity.User;
import org.example.course_server.service.ParkingSpotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/parking-spots")
public class ParkingSpotController {

    private final ParkingSpotService parkingSpotService;

    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

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
}

