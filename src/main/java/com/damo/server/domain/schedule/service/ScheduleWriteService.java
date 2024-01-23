package com.damo.server.domain.schedule.service;

import com.damo.server.application.config.user_details.SecurityUserUtil;
import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
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
    private final SecurityUserUtil securityUserUtil;

    @Transactional
    public void addSchedule(final RequestCreateScheduleDto scheduleDto) {
        if (scheduleRepository.findByEventAndEventDateAndUserId(scheduleDto.event(), scheduleDto.eventDate(), securityUserUtil.getId()).isPresent()) {
            throw new CustomException(CustomErrorCode.BAD_REQUEST, "일정 내에서 동일한 기록이 존재");
        }
        final Schedule schedule = Schedule.from(scheduleDto, securityUserUtil.getId());
        scheduleRepository.save(schedule);
    }

    @Transactional
    public void patchScheduleById(final RequestUpdateScheduleDto scheduleDto, final Long scheduleId) {
        final Schedule schedule = scheduleRepository.findByIdAndUserId(scheduleId, securityUserUtil.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND, "수정할 내역을 찾을 수 없음"));
        schedule.changeSchedule(scheduleDto);
    }

    public void removeScheduleById(final Long scheduleId) {
        scheduleRepository.findByIdAndUserId(scheduleId, securityUserUtil.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND, "삭제할 일정을 찾을 수 없음"));
        scheduleRepository.deleteById(scheduleId);
    }
}
