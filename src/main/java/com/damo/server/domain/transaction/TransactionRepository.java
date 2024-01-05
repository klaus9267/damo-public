package com.damo.server.domain.transaction;

import com.damo.server.domain.transaction.dto.TransactionDto;
import com.damo.server.domain.transaction.entity.Transaction;
import com.damo.server.domain.transaction.entity.TransactionAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("""
           SELECT count(t.id) > 0
           FROM Transaction t
           LEFT JOIN FETCH  Schedule s ON t.id = s.transaction.id
           WHERE t.person.id = :personId AND s.eventDate = :eventDate AND s.event = :event
           """)
    Boolean existsByPersonId(@Param("eventDate") final LocalDateTime eventDate, @Param("personId") final Long personId, @Param("event") final String event);


    @EntityGraph(attributePaths = "schedule")
    Optional<Transaction> findByIdAndUserId(@Param("id") final Long id, @Param("userId") final Long userId);

    @Query("""
           SELECT new com.damo.server.domain.transaction.dto.TransactionDto(t, p, s)
           FROM Transaction t
                LEFT JOIN FETCH  Person p ON t.person.id = p.id
                LEFT JOIN FETCH  Schedule s ON t.schedule.id = s.id
           WHERE p.user.id = :userId
                AND (:startedAt IS NULL OR s.eventDate >= :startedAt)
                AND (:endedAt IS NULL OR s.eventDate <= :endedAt)
                AND ('TOTAL' = :action  OR t.transactionAmount.action = :action)
           """)
    Page<TransactionDto> findAllByUserId(
            final Pageable pageable,
            @Param("userId") final Long userId,
            @Param("startedAt") final LocalDateTime startedAt,
            @Param("endedAt") final LocalDateTime endedAt,
            @Param("action") final TransactionAction action
    );

    @Query("""
           SELECT new com.damo.server.domain.transaction.TransactionTotalAmount(
                 new com.damo.server.domain.transaction.entity.TransactionAmount(
                     SUM(CASE WHEN t.transactionAmount.action = 'GIVING' THEN t.transactionAmount.amount ELSE 0 END), 'GIVING'
                 ),
                 new com.damo.server.domain.transaction.entity.TransactionAmount(
                     SUM(CASE WHEN t.transactionAmount.action = 'RECEIVING' THEN t.transactionAmount.amount ELSE 0 END), 'RECEIVING'
                 )
           )
           FROM Transaction t
                LEFT JOIN FETCH  Person p ON t.person.id = p.id
           WHERE t.user.id = :userId
           """)
    TransactionTotalAmount findTotalAmount(@Param("userId") final Long userId);

    @Query("""
           SELECT new com.damo.server.domain.transaction.TransactionTotalAmount(
                 new com.damo.server.domain.transaction.entity.TransactionAmount(
                     SUM(CASE WHEN t.transactionAmount.action = 'GIVING' THEN t.transactionAmount.amount ELSE 0 END), 'GIVING'
                 ),
                 new com.damo.server.domain.transaction.entity.TransactionAmount(
                     SUM(CASE WHEN t.transactionAmount.action = 'RECEIVING' THEN t.transactionAmount.amount ELSE 0 END), 'RECEIVING'
                 )
           )
           FROM Transaction t
                LEFT JOIN FETCH  Person p ON t.person.id = p.id
                LEFT JOIN FETCH  Schedule s ON t.id = s.transaction.id
           WHERE t.user.id = :userId AND s.eventDate >= :startedAt
           """)
    TransactionTotalAmount readRecentAmounts(@Param("userId") final Long userId, @Param("startedAt") final LocalDateTime startedAt);
}
