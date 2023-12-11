package com.damo.server.domain.schedule;

import com.damo.server.domain.schedule.dto.ScheduleDto;
import com.damo.server.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Boolean existsByDateAndEventAndPersonId(LocalDate date, String event, Long personId);

    @Query("SELECT new com.damo.server.domain.schedule.dto.ScheduleDto(s, p) FROM Schedule s LEFT JOIN FETCH  Person p ON s.person.id = p.id WHERE s.id = :scheduleId")
    Optional<ScheduleDto> findOne(@Param("scheduleId") Long scheduleId);
}
