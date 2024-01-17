package com.damo.server.domain.schedule.service;

import com.damo.server.application.handler.exception.BadRequestException;
import com.damo.server.application.handler.exception.NotFoundException;
import com.damo.server.domain.schedule.Schedule;
import com.damo.server.domain.schedule.ScheduleRepository;
import com.damo.server.domain.schedule.dto.RequestCreateScheduleDto;
import com.damo.server.domain.schedule.dto.RequestUpdateScheduleDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class ScheduleWriteService {
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public void addSchedule(final RequestCreateScheduleDto scheduleDto, final Long userId) {
        if (scheduleRepository.findByEventAndEventDateAndUserId(scheduleDto.event(), scheduleDto.eventDate(), userId).isPresent()) {
            throw new BadRequestException("일정 내에서 동일한 기록이 존재");
        }
        final Schedule schedule = Schedule.from(scheduleDto, userId);
        scheduleRepository.save(schedule);
    }

    @Transactional
    public void patchScheduleById(final RequestUpdateScheduleDto scheduleDto, final Long scheduleId, final Long userId) {
        final Schedule schedule = scheduleRepository.findByIdAndUserId(scheduleId, userId).orElseThrow(() -> new NotFoundException("수정할 내역을 찾을 수 없음"));
        schedule.changeSchedule(scheduleDto);
    }
  
    public void removeScheduleById(final Long scheduleId, final Long userId) {
        scheduleRepository.findByIdAndUserId(scheduleId, userId).orElseThrow(() -> new NotFoundException("삭제할 일정을 찾을 수 없음"));
        scheduleRepository.deleteById(scheduleId);
    }
}
