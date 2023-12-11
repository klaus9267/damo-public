package com.damo.server.domain.schedule;

import com.damo.server.application.handler.exception.BadRequestException;
import com.damo.server.domain.person.Person;
import com.damo.server.domain.person.dto.PersonDto;
import com.damo.server.domain.person.dto.RequestPersonDto;
import com.damo.server.domain.schedule.dto.RequestScheduleDto;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import com.damo.server.domain.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

@AllArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public ScheduleDto save(final RequestScheduleDto scheduleDto) {
        if (scheduleRepository.existsByDateAndEventAndPersonId(scheduleDto.date(), scheduleDto.event(), scheduleDto.personId())) {
            throw new BadRequestException("스케줄 내에서 동일한 기록이 존재");
        };

        return ScheduleMapper.toDto(scheduleRepository.save(ScheduleMapper.toEntity(scheduleDto)));
    }

    public ScheduleDto readSchedule(final Long scheduleId) {
        return scheduleRepository.findOne(scheduleId).orElseThrow(() -> new NotFoundException("조회할 대상을 찾을 수 없음"));
    }

    public void removeScheduleById(final Long scheduleId) {
        this.readSchedule(scheduleId);
        // TODO: security로 personId 받으면 같은 유저인지 판단 조건 추가
        scheduleRepository.deleteById(scheduleId);
    }
}
