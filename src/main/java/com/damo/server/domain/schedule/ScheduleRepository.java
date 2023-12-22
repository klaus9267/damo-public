package com.damo.server.domain.schedule;

import com.damo.server.domain.schedule.dto.ScheduleDto;
import com.damo.server.domain.schedule.entity.Schedule;
import com.damo.server.domain.schedule.entity.ScheduleTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Boolean existsByEventDateAndEventAndPersonId(LocalDateTime eventDate, String event, Long personId);

    Optional<Schedule> findByIdAndUserId(@Param("scheduleId") final Long scheduleId, @Param("userId") final Long userId);

    @Query("""
           SELECT new com.damo.server.domain.schedule.dto.ScheduleDto(s, p) 
           FROM Schedule s 
                LEFT JOIN FETCH  Person p ON s.person.id = p.id 
           WHERE p.user.id = :userId 
                AND (:startedAt IS NULL OR s.eventDate >= :startedAt)
                AND (:endedAt IS NULL OR s.eventDate <= :endedAt)
                AND ('TOTAL' = :transaction  OR s.transaction = :transaction)
           """)
    Page<ScheduleDto> findAllByUserId(
            final Pageable pageable,
            @Param("userId") final Long userId,
            @Param("startedAt") final LocalDateTime startedAt,
            @Param("endedAt") final LocalDateTime endedAt,
            @Param("transaction") final ScheduleTransaction transaction
    );

    @Query("""
           SELECT new com.damo.server.domain.schedule.ScheduleAmount(
                SUM(CASE WHEN s.transaction = com.damo.server.domain.schedule.entity.ScheduleTransaction.GIVING THEN s.amount ELSE 0 END),
                SUM(CASE WHEN s.transaction = com.damo.server.domain.schedule.entity.ScheduleTransaction.RECEIVING THEN s.amount ELSE 0 END)
                ) 
           FROM Schedule s 
                LEFT JOIN FETCH  Person p ON s.person.id = p.id 
           WHERE p.user.id = :userId 
           """)
    Optional<ScheduleAmount> findTotalAmount(final Long userId);

    @Query("""
           SELECT new com.damo.server.domain.schedule.ScheduleAmount(
                SUM(CASE WHEN s.transaction = com.damo.server.domain.schedule.entity.ScheduleTransaction.GIVING THEN s.amount ELSE 0 END),
                SUM(CASE WHEN s.transaction = com.damo.server.domain.schedule.entity.ScheduleTransaction.RECEIVING THEN s.amount ELSE 0 END)
                ) 
           FROM Schedule s 
                LEFT JOIN FETCH  Person p ON s.person.id = p.id 
           WHERE p.user.id = :userId 
                AND (:startedAt IS NULL OR s.eventDate >= :startedAt)
                AND (:endedAt IS NULL OR s.eventDate <= :endedAt)
           """)
    ScheduleAmount findTermTotalAmount(final Long userId, final LocalDateTime startedAt, final LocalDateTime endedAt);
}
