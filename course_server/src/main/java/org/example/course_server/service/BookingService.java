package org.example.course_server.service;

import org.example.course_server.entity.Booking;
import org.example.course_server.entity.User;
import org.example.course_server.repository.BookingRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepo bookingRepo;

    public BookingService(BookingRepo bookingRepo) {
        this.bookingRepo = bookingRepo;
    }

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

    public List<Booking> findAll() {
        return bookingRepo.findAll();
    }

    public Optional<Booking> findById(Long id) {
        return bookingRepo.findById(id);
    }

    public List<Booking> findByUser(User user) {
        return bookingRepo.findByUser(user);
    }
}

