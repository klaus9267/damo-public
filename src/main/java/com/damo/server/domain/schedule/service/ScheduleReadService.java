package com.damo.server.domain.schedule.service;

import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import com.damo.server.domain.common.pagination.param.SchedulePaginationParam;
import com.damo.server.domain.schedule.Schedule;
import com.damo.server.domain.schedule.ScheduleRepository;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import com.damo.server.domain.schedule.dto.SchedulePaginationResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ScheduleReadService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleDto readSchedule(final Long scheduleId, final Long userId) {
        final Schedule schedule = scheduleRepository.findByIdAndUserId(scheduleId, userId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND, "조회할 일정을 찾을 수 없음"));
        return ScheduleDto.from(schedule);
    }

    public SchedulePaginationResponseDto readScheduleByEventDate(final SchedulePaginationParam paginationParam, final Long userId) {
        final Page<ScheduleDto> schedulePage = scheduleRepository.findAllScheduleByEventDate(paginationParam.toPageable(), userId, paginationParam.getYear(), paginationParam.getMonth(), paginationParam.getKeyword());
        return SchedulePaginationResponseDto.from(schedulePage);
    }
}
