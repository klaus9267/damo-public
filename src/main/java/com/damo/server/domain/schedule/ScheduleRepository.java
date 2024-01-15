package com.damo.server.domain.schedule;

import com.damo.server.domain.schedule.dto.ScheduleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByEventAndEventDateAndUserId(final String event, final LocalDateTime eventDate, final Long userId);

    Optional<Schedule> findByIdAndUserId(final Long id, final Long userId);

    @Query("""
           SELECT new com.damo.server.domain.schedule.dto.ScheduleDto(s,t)
           FROM Schedule s
                LEFT JOIN FETCH  s.transaction t
                LEFT JOIN FETCH  t.person p
           WHERE p.user.id = :userId
                AND FUNCTION('MONTH', s.eventDate) = :month
                AND FUNCTION('YEAR', s.eventDate) = :year
           """)
    Page<ScheduleDto> findAllScheduleByEventDate(final Pageable pageable, @Param("userId") final Long userId, @Param("year") final int year, @Param("month") final int month);
}
