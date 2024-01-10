package com.damo.server.domain.schedule.service;

import com.damo.server.application.handler.exception.BadRequestException;
import com.damo.server.domain.schedule.Schedule;
import com.damo.server.domain.schedule.ScheduleRepository;
import com.damo.server.domain.schedule.dto.RequestCreateScheduleDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ScheduleWriteService {
    private final ScheduleRepository scheduleRepository;

    public void addSchedule(final RequestCreateScheduleDto scheduleDto, final Long userId) {
        if (scheduleRepository.findByEventAndEventDateAndUserId(scheduleDto.event(), scheduleDto.eventDate(), userId).isPresent()) {
            throw new BadRequestException("일정 내에서 동일한 기록이 존재");
        }
        final Schedule schedule = Schedule.from(scheduleDto, userId);
        scheduleRepository.save(schedule);
    }
}
