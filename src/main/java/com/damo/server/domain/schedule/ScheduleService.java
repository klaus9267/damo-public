package com.damo.server.domain.schedule;

import com.damo.server.domain.schedule.dto.ScheduleDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@AllArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleDto readSchedule(final Long scheduleId) {
        return scheduleRepository.findOne(scheduleId).orElseThrow(() -> new NotFoundException("조회할 대상을 찾을 수 없음"));
    }
}
