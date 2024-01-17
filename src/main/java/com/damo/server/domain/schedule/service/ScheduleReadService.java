package com.damo.server.domain.schedule.service;

import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import com.damo.server.domain.schedule.Schedule;
import com.damo.server.domain.schedule.ScheduleRepository;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ScheduleReadService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleDto readSchedule(final Long scheduleId, final Long userId) {
        final Schedule schedule = scheduleRepository.findByIdAndUserId(scheduleId, userId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND, "조회할 일정을 찾을 수 없음"));
        return ScheduleDto.from(schedule);
    }
}
