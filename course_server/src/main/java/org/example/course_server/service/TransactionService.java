package org.example.course_server.service;

import org.example.course_server.entity.Transaction;
import org.example.course_server.entity.User;
import org.example.course_server.repository.TransactionRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepo transactionRepo;

    public TransactionService(
            TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    public Transaction createTransaction(User user, int amount) {
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(amount);
        transaction.setStatus("IN_PROGRESS");  // Статус "В процессе"

        return transactionRepo.save(transaction);  // Сохраняем без привязки к Booking
    }

    public List<Transaction> findByUserId(Long userId) {
        return transactionRepo.findByUserId(userId);
    }

    public Optional<Transaction> findById(Long id) {
        return transactionRepo.findById(id);
    }
}