package com.damo.server.domain.schedule;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByEventAndEventDateAndUserId(final String event, final LocalDateTime eventDate, final Long userId);
}
