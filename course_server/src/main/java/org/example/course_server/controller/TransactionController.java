package org.example.course_server.controller;

import org.example.course_server.entity.*;
import org.example.course_server.service.*;
import org.example.course_server.repository.TransactionRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Контроллер для работы с транзакциями.
 * Обрабатывает запросы, связанные с созданием, просмотром, обновлением статуса транзакций и привязкой к бронированию.
 */
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

    /**
     * Создает новую транзакцию для пользователя.
     *
     * @param transactionData данные транзакции, содержащие ID пользователя и сумму.
     * @return {@link ResponseEntity} с созданной транзакцией или сообщением об ошибке.
     */
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

    /**
     * Возвращает список всех транзакций.
     *
     * @return {@link ResponseEntity} со списком транзакций.
     */
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionRepo.findAll();
        return ResponseEntity.ok(transactions);
    }

    /**
     * Возвращает транзакции для указанного пользователя.
     *
     * @param userId ID пользователя.
     * @return {@link ResponseEntity} со списком транзакций пользователя.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionsByUser(@PathVariable Long userId) {
        List<Transaction> transactions = transactionService.findByUserId(userId);
        return ResponseEntity.ok(transactions);
    }

    /**
     * Возвращает транзакцию по её идентификатору.
     *
     * @param id ID транзакции.
     * @return {@link ResponseEntity} с транзакцией или статус 404, если транзакция не найдена.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        Optional<Transaction> transaction = transactionService.findById(id);
        return transaction.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Обновляет статус указанной транзакции.
     *
     * @param id   ID транзакции.
     * @param body данные, содержащие новый статус транзакции.
     * @return {@link ResponseEntity} с сообщением о результатах обновления.
     */
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

    /**
     * Привязывает указанную транзакцию к бронированию.
     *
     * @param transactionId ID транзакции.
     * @param bookingId     ID бронирования.
     * @return {@link ResponseEntity} с сообщением о результатах привязки.
     */
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