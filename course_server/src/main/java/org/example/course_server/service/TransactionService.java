package org.example.course_server.service;

import org.example.course_server.entity.*;
import org.example.course_server.repository.TransactionRepo;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Сервис для работы с сущностью транзакции.
 * Предоставляет методы для создания и поиска транзакций.
 */
@Service
public class TransactionService {

    private final TransactionRepo transactionRepo;

    public TransactionService(TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    /**
     * Создаёт новую транзакцию для пользователя.
     *
     * @param user   Пользователь, создающий транзакцию.
     * @param amount Сумма транзакции.
     * @return Сохранённая транзакция.
     */
    public Transaction createTransaction(User user, int amount) {
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(amount);
        transaction.setStatus("IN_PROGRESS");  // Статус "В процессе"
        return transactionRepo.save(transaction);
    }

    /**
     * Находит транзакции пользователя по ID.
     *
     * @param userId ID пользователя.
     * @return Список транзакций пользователя.
     */
    public List<Transaction> findByUserId(Long userId) {
        return transactionRepo.findByUserId(userId);
    }

    /**
     * Находит транзакцию по ID.
     *
     * @param id ID транзакции.
     * @return Опционально найденная транзакция.
     */
    public Optional<Transaction> findById(Long id) {
        return transactionRepo.findById(id);
    }
}