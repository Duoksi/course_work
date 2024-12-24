package org.example.course_server.controller;

import org.example.course_server.entity.*;
import org.example.course_server.repository.*;
import org.example.course_server.service.ParkingSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Контроллер для работы с парковочными местами.
 * Обрабатывает запросы, связанные с созданием, просмотром и получением статистики парковочных мест.
 */
@RestController
@RequestMapping("/api/parking-spots")
public class ParkingSpotController {

    @Autowired
    private ParkingSpotService parkingSpotService;

    @Autowired
    private ParkingSpotRepo parkingSpotRepo;

    @Autowired
    private BookingRepo bookingRepo;

    /**
     * Регистрирует новое парковочное место.
     *
     * @param parkingSpot объект {@link ParkingSpot}, представляющий данные нового парковочного места.
     * @return {@link ResponseEntity} с зарегистрированным парковочным местом.
     */
    @PostMapping
    public ResponseEntity<ParkingSpot> registerParkingSpot(@RequestBody ParkingSpot parkingSpot) {
        ParkingSpot createdParkingSpot = parkingSpotService.registerParkingSpot(parkingSpot);
        return ResponseEntity.ok(createdParkingSpot);
    }

    /**
     * Получает список всех парковочных мест.
     *
     * @return {@link ResponseEntity} с перечнем всех парковочных мест.
     */
    @GetMapping
    public ResponseEntity<List<ParkingSpot>> getAllParkingSpots() {
        List<ParkingSpot> spots = parkingSpotService.findAll();
        return ResponseEntity.ok(spots);
    }

    /**
     * Получает парковочное место по его идентификатору.
     *
     * @param id идентификатор парковочного места.
     * @return {@link ResponseEntity} с данными парковочного места или статусом 404, если место не найдено.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpot> getParkingSpotById(@PathVariable Long id) {
        Optional<ParkingSpot> spot = parkingSpotService.findById(id);
        return spot.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Получает статистику парковочных мест.
     *
     * <p>Статистика включает:
     * <ul>
     *     <li>Количество занятых мест для каждого торгового центра (ТЦ).</li>
     *     <li>Общее количество мест для каждого ТЦ.</li>
     * </ul>
     *
     * @return {@link ResponseEntity} с картой, где ключ — название ТЦ, а значение — массив из двух элементов:
     *         <code>[занятые места, всего мест]</code>.
     */
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