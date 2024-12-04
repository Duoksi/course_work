package org.example.course_server.service;

import org.example.course_server.entity.ParkingSpot;
import org.example.course_server.repository.ParkingSpotRepo;
import org.example.course_server.repository.BookingRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingSpotService {

    private final ParkingSpotRepo parkingSpotRepo;
    private final BookingRepo bookingRepo;

    public ParkingSpotService(ParkingSpotRepo parkingSpotRepo, BookingRepo bookingRepo) {
        this.parkingSpotRepo = parkingSpotRepo;
        this.bookingRepo = bookingRepo;
    }

    public List<ParkingSpot> findAll() {
        return parkingSpotRepo.findAll();
    }

    public boolean isSpotOccupied(int spotNumber, String tcName) {
        return bookingRepo.isParkingSpotOccupied(spotNumber, tcName, LocalDateTime.now());
    }

    public Optional<ParkingSpot> findById(Long id) {
        return parkingSpotRepo.findById(id);
    }

    public ParkingSpot registerParkingSpot(ParkingSpot parkingSpot) {return parkingSpotRepo.save(parkingSpot); }
}

