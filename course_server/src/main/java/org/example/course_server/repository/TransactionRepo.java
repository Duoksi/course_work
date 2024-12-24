package org.example.course_server.repository;

import org.example.course_server.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Репозиторий для работы с сущностью транзакции.
 * Предоставляет методы для выполнения операций с данными транзакций.
 */
public interface TransactionRepo extends JpaRepository<Transaction, Long> {

    /**
     * Находит все транзакции, связанные с указанным пользователем.
     *
     * @param userId ID пользователя.
     * @return Список транзакций пользователя.
     */
    List<Transaction> findByUserId(Long userId);

    /**
     * Находит все транзакции, связанные с указанным бронированием.
     *
     * @param bookingId ID бронирования.
     * @return Список транзакций для указанного бронирования.
     */
    List<Transaction> findByBookingId(Long bookingId);
}