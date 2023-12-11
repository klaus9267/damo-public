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

    public void removeScheduleById(final Long scheduleId) {
        // TODO: security로 personId 받으면 같은 유저인지 판단 조건 추가
        scheduleRepository.deleteById(scheduleId);
    }
}
