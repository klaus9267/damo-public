package com.damo.server.domain.schedule;

import com.damo.server.domain.schedule.dto.ScheduleDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT new com.damo.server.domain.schedule.dto.ScheduleDto(p) FROM Schedule p  WHERE p.id = :scheduleId")
    Optional<ScheduleDto> findScheduleById(Long scheduleId);
}
