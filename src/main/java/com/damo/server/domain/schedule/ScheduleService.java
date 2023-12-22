package com.damo.server.domain.schedule;

import com.damo.server.application.handler.exception.BadRequestException;
import com.damo.server.application.handler.exception.NotFoundException;
import com.damo.server.domain.common.pagination.param.SchedulePaginationParam;
import com.damo.server.domain.schedule.dto.RequestScheduleDto;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import com.damo.server.domain.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@AllArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public void save(final RequestScheduleDto scheduleDto, final Long userId) {
        if (scheduleRepository.existsByEventDateAndEventAndPersonId(scheduleDto.eventDate(), scheduleDto.event(), scheduleDto.personId())) {
            throw new BadRequestException("스케줄 내에서 동일한 기록이 존재");
        }
        scheduleRepository.save(Schedule.from(scheduleDto, userId));
    }

    public ScheduleAmount readTotalAmounts(final Long userId) {
        return scheduleRepository.findTotalAmount(userId);
    }

    public ScheduleAmount readRecentAmounts(final Long userId, final LocalDateTime startedAt) {
        return scheduleRepository.readRecentAmounts(userId, startedAt);
    }

    public ScheduleDto readSchedule(final Long scheduleId, final Long userId) {
        final Schedule schedule = scheduleRepository.findByIdAndUserId(scheduleId, userId).orElseThrow(() -> new NotFoundException("조회할 대상을 찾을 수 없음"));
        return ScheduleDto.from(schedule);
    }

    public Page<ScheduleDto> readScheduleList(final SchedulePaginationParam param, final Long userId) {
        return scheduleRepository.findAllByUserId(param.toPageable(), userId, param.getStartedAt(), param.getEndedAt(), param.getTransaction());
    }

    @Transactional
    public void patchScheduleById(final RequestScheduleDto scheduleDto, final Long scheduleId, final Long userId) {
        final Schedule schedule = scheduleRepository.findByIdAndUserId(scheduleId, userId).orElseThrow(() -> new NotFoundException("수정할 대상을 찾을 수 없음"));
        schedule.changeInfo(scheduleDto);
    }

    public void removeScheduleById(final Long scheduleId, final Long userId) {
        scheduleRepository.findByIdAndUserId(scheduleId, userId).orElseThrow(() -> new NotFoundException("조회할 대상을 찾을 수 없음"));
        scheduleRepository.deleteById(scheduleId);
    }
}
