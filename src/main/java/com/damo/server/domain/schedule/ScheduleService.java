package com.damo.server.domain.schedule;

import com.damo.server.domain.schedule.dto.ScheduleDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleDto readSchedule(Long scheduleId) {
        return scheduleRepository.findScheduleById(scheduleId).orElseThrow(()->new RuntimeException("조회할 대상을 찾을 수 없음"));
    }
}
