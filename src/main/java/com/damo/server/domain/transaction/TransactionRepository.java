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
    Boolean existsByEventDateAndEventAndPersonId(LocalDateTime eventDate, String event, Long personId);

    Optional<Transaction> findByIdAndUserId(@Param("scheduleId") final Long scheduleId, @Param("userId") final Long userId);

    @Query("""
           SELECT new com.damo.server.domain.transaction.dto.TransactionDto(s, p) 
           FROM Transaction s 
                LEFT JOIN FETCH  Person p ON s.person.id = p.id 
           WHERE p.user.id = :userId 
                AND (:startedAt IS NULL OR s.eventDate >= :startedAt)
                AND (:endedAt IS NULL OR s.eventDate <= :endedAt)
                AND ('TOTAL' = :action  OR s.transaction = :action)
           """)
    Page<TransactionDto> findAllByUserId(
            final Pageable pageable,
            @Param("userId") final Long userId,
            @Param("startedAt") final LocalDateTime startedAt,
            @Param("endedAt") final LocalDateTime endedAt,
            @Param("action") final TransactionAction action
                                        );

    @Query("""
           SELECT new com.damo.server.domain.transaction.ScheduleAmount(
                SUM(CASE WHEN s.transaction = com.damo.server.domain.transaction.entity.TransactionAction.GIVING THEN s.amount ELSE 0 END),
                SUM(CASE WHEN s.transaction = com.damo.server.domain.transaction.entity.TransactionAction.RECEIVING THEN s.amount ELSE 0 END)
                ) 
           FROM Transaction s 
                LEFT JOIN FETCH  Person p ON s.person.id = p.id 
           WHERE s.user.id = :userId 
           """)
    ScheduleAmount findTotalAmount(@Param("userId") final Long userId);

    @Query("""
           SELECT new com.damo.server.domain.transaction.ScheduleAmount(
                SUM(CASE WHEN s.transaction = com.damo.server.domain.transaction.entity.TransactionAction.GIVING THEN s.amount ELSE 0 END),
                SUM(CASE WHEN s.transaction = com.damo.server.domain.transaction.entity.TransactionAction.RECEIVING THEN s.amount ELSE 0 END)
                ) 
           FROM Transaction s 
                LEFT JOIN FETCH  Person p ON s.person.id = p.id 
           WHERE s.user.id = :userId AND s.eventDate >= :startedAt
           """)
    ScheduleAmount readRecentAmounts(@Param("userId") final Long userId, @Param("startedAt") final LocalDateTime startedAt);
}
