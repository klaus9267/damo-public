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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Boolean existsByDateAndEventAndPersonId(LocalDateTime date, String event, Long personId);

    @Query("SELECT new com.damo.server.domain.schedule.dto.ScheduleDto(s, p) FROM Schedule s LEFT JOIN FETCH  Person p ON s.person.id = p.id WHERE s.id = :scheduleId")
    Optional<ScheduleDto> findOne(@Param("scheduleId") Long scheduleId);

    @Query("SELECT new com.damo.server.domain.schedule.dto.ScheduleDto(s, p) FROM Schedule s LEFT JOIN FETCH  Person p ON s.person.id = p.id " +
           "WHERE p.user.id = :userId AND ((:startedAt IS NULL AND :endedAt IS NULL ) OR (s.date BETWEEN :startedAt AND :endedAt))")
    Page<ScheduleDto> findAllByUserId(final Pageable pageable, @Param("userId") Long userId, @Param("startedAt") final LocalDateTime startedAt, @Param("endedAt") final LocalDateTime endedAt);

    @Query("SELECT new com.damo.server.domain.schedule.ScheduleAmount(SUM(s.amount), s.transaction) FROM Schedule s LEFT JOIN FETCH  Person p ON s.person.id = p.id WHERE p.user.id = :userId GROUP BY s.transaction")
    List<ScheduleAmount> findTotalAmount(Long userId);
}
