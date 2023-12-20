package com.damo.server.domain.schedule;

import com.damo.server.application.handler.exception.BadRequestException;
import com.damo.server.application.handler.exception.NotFoundException;
import com.damo.server.domain.common.pagination.CustomSchedulePage;
import com.damo.server.domain.common.pagination.param.SchedulePaginationParam;
import com.damo.server.domain.schedule.dto.RequestScheduleDto;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import com.damo.server.domain.schedule.entity.Schedule;
import com.damo.server.domain.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@AllArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserService userService;

    @Transactional
    public void save(final RequestScheduleDto scheduleDto) {
        if (scheduleRepository.existsByDateAndEventAndPersonId(scheduleDto.date(), scheduleDto.event(), scheduleDto.personId())) {
            throw new BadRequestException("스케줄 내에서 동일한 기록이 존재");
        }
        scheduleRepository.save(Schedule.from(scheduleDto));
    }

    public ScheduleAmount readTotalAmounts(final Long userId) {
        return scheduleRepository.findTotalAmount(userId).orElseThrow(() -> new NotFoundException("조회할 대상을 찾을 수 없음"));
    }

    public ScheduleDto readSchedule(final Long scheduleId, final Long userId) {
        final Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new NotFoundException("조회할 대상을 찾을 수 없음"));
        if (!schedule.getPerson().getUser().getId().equals(userId)) {
            throw new BadRequestException("다른 사용자의 스케줄");
        }
        return ScheduleDto.from(schedule);
    }

    public CustomSchedulePage readScheduleList(final SchedulePaginationParam param, final Long userId) {
        Page<ScheduleDto> page = scheduleRepository.findAllByUserId(param.toPageable(), userId, param.getStartedAt(), param.getEndedAt(), param.getTransaction());
        return param.getStartedAt() != null && param.getEndedAt() != null
               ? new CustomSchedulePage(page)
               : new CustomSchedulePage(page, scheduleRepository.findTermTotalAmount(userId, param.getStartedAt(), param.getEndedAt()));
    }

    @Transactional
    public void patchScheduleById(final RequestScheduleDto scheduleDto, final Long scheduleId, final Long userId) {
        final Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new NotFoundException("수정할 대상을 찾을 수 없음"));
        if (!schedule.getPerson().getUser().getId().equals(userId)) {
            throw new BadRequestException("다른 사용자의 스케줄");
        }
        schedule.changeInfo(scheduleDto);
    }

    public void removeScheduleById(final Long scheduleId, final Long userId) {
        final Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new NotFoundException("조회할 대상을 찾을 수 없음"));
        if (!schedule.getPerson().getUser().getId().equals(userId)) {
            throw new BadRequestException("다른 사용자의 스케줄");
        }
        scheduleRepository.deleteById(scheduleId);
    }
}
