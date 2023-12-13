package com.damo.server.domain.schedule;

import com.damo.server.domain.schedule.dto.ScheduleDto;
import com.damo.server.domain.schedule.dto.ScheduleWithPersonDto;
import com.damo.server.domain.schedule.entity.Schedule;
import com.damo.server.domain.schedule.entity.ScheduleTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Boolean existsByDateAndEventAndPersonId(LocalDateTime date, String event, Long personId);

    @Query("SELECT new com.damo.server.domain.schedule.dto.ScheduleDto(s, p) FROM Schedule s LEFT JOIN FETCH  Person p ON s.person.id = p.id WHERE s.id = :scheduleId")
    Optional<ScheduleDto> findOne(@Param("scheduleId") Long scheduleId);

    @Query("SELECT new com.damo.server.domain.schedule.dto.ScheduleWithPersonDto(s, p) FROM Schedule s LEFT JOIN FETCH  Person p ON s.person.id = p.id WHERE (s.transaction = :transaction AND p.user.id = :userId) ORDER BY s.date")
    Page<ScheduleWithPersonDto> findAllWithTotalAmount(Pageable pageable, ScheduleTransaction transaction, Long userId);
}
