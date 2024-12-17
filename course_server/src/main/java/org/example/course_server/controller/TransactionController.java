package org.example.course_server.controller;

import org.example.course_server.entity.Booking;
import org.example.course_server.entity.ParkingSpot;
import org.example.course_server.entity.Transaction;
import org.example.course_server.entity.User;
import org.example.course_server.service.BookingService;
import org.example.course_server.service.UserService;
import org.example.course_server.repository.TransactionRepo;
import org.example.course_server.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionRepo transactionRepo;
    private final BookingService bookingService;
    private final UserService userService;

    public TransactionController(TransactionService transactionService,
                                 TransactionRepo transactionRepo,
                                 BookingService bookingService,
                                 UserService userService) {
        this.transactionService = transactionService;
        this.transactionRepo = transactionRepo;
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody Map<String, Object> transactionData) {
        Long userId = ((Number) transactionData.get("userId")).longValue();
        int amount = (int) transactionData.get("amount");

        Optional<User> userOptional = userService.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Пользователь не найден.");
        }

        Transaction transaction = transactionService.createTransaction(userOptional.get(), amount);
        return ResponseEntity.ok(transaction);  // Возвращаем созданную транзакцию с ID
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionRepo.findAll();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionsByUser(@PathVariable Long userId) {
        List<Transaction> transactions = transactionService.findByUserId(userId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        Optional<Transaction> transaction = transactionService.findById(id);
        return transaction.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateTransactionStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String newStatus = body.get("newStatus");

        Optional<Transaction> optionalTransaction = transactionService.findById(id);

        if (optionalTransaction.isEmpty()) {
            return ResponseEntity.badRequest().body("Транзакция не найдена.");
        }

        Transaction transaction = optionalTransaction.get();
        transaction.setStatus(newStatus);
        transactionRepo.save(transaction); // Сохраняем изменения

        return ResponseEntity.ok("Статус транзакции обновлён на " + newStatus);
    }

    @PutMapping("/update-booking")
    public ResponseEntity<?> updateBookingId(@RequestParam Long transactionId, @RequestParam Long bookingId) {
        Optional<Transaction> optionalTransaction = transactionService.findById(transactionId);
        if (optionalTransaction.isEmpty()) {
            return ResponseEntity.badRequest().body("Транзакция не найдена.");
        }

        Optional<Booking> bookingOptional = bookingService.findById(bookingId);
        if (bookingOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Бронирование не найдено.");
        }

        Transaction transaction = optionalTransaction.get();
        transaction.setBooking(bookingOptional.get());  // Привязываем транзакцию к бронированию
        transactionRepo.save(transaction);

        return ResponseEntity.ok("Booking ID обновлён для транзакции " + transactionId);
    }

}
