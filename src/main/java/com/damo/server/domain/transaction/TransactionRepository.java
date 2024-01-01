package com.damo.server.domain.transaction;

import com.damo.server.domain.transaction.dto.TransactionDto;
import com.damo.server.domain.transaction.entity.Transaction;
import com.damo.server.domain.transaction.entity.TransactionAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Boolean existsByEventDateAndPersonId(LocalDateTime eventDate, Long personId);

    Optional<Transaction> findByIdAndUserId(@Param("scheduleId") final Long scheduleId, @Param("userId") final Long userId);

    @Query("""
           SELECT new com.damo.server.domain.transaction.dto.TransactionDto(t, p)
           FROM Transaction t
                LEFT JOIN FETCH  Person p ON t.person.id = p.id
           WHERE p.user.id = :userId
                AND (:startedAt IS NULL OR t.eventDate >= :startedAt)
                AND (:endedAt IS NULL OR t.eventDate <= :endedAt)
                AND ('TOTAL' = :action  OR t.transaction.action = :action)
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
                     SUM(CASE WHEN t.transaction.action = 'GIVING' THEN t.transaction.amount ELSE 0 END), 'GIVING'
                 ),
                 new com.damo.server.domain.transaction.entity.TransactionAmount(
                     SUM(CASE WHEN t.transaction.action = 'RECEIVING' THEN t.transaction.amount ELSE 0 END), 'RECEIVING'
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
                     SUM(CASE WHEN t.transaction.action = 'GIVING' THEN t.transaction.amount ELSE 0 END), 'GIVING'
                 ),
                 new com.damo.server.domain.transaction.entity.TransactionAmount(
                     SUM(CASE WHEN t.transaction.action = 'RECEIVING' THEN t.transaction.amount ELSE 0 END), 'RECEIVING'
                 )
           )
           FROM Transaction t
                LEFT JOIN FETCH  Person p ON t.person.id = p.id
           WHERE t.user.id = :userId AND t.eventDate >= :startedAt
           """)
    TransactionTotalAmount readRecentAmounts(@Param("userId") final Long userId, @Param("startedAt") final LocalDateTime startedAt);
}
