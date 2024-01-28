package com.damo.server.domain.schedule.service;

import com.damo.server.application.config.user_details.SecurityUserUtil;
import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import com.damo.server.domain.common.pagination.param.SchedulePaginationParam;
import com.damo.server.domain.schedule.Schedule;
import com.damo.server.domain.schedule.ScheduleRepository;
import com.damo.server.domain.schedule.dto.ScheduleWithTransactionDto;
import com.damo.server.domain.schedule.dto.SchedulePaginationResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ScheduleReadService {
    private final ScheduleRepository scheduleRepository;
    private final SecurityUserUtil securityUserUtil;

    public ScheduleWithTransactionDto readSchedule(final Long scheduleId) {
        final Schedule schedule = scheduleRepository.findByIdAndUserId(scheduleId, securityUserUtil.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND, "조회할 일정을 찾을 수 없음"));
        return ScheduleWithTransactionDto.from(schedule);
    }

    public SchedulePaginationResponseDto readScheduleByEventDate(final SchedulePaginationParam paginationParam) {
        final Page<ScheduleWithTransactionDto> schedulePage = scheduleRepository.findAllScheduleByEventDate(paginationParam.toPageable(), securityUserUtil.getId(), paginationParam.getYear(), paginationParam.getMonth(), paginationParam.getKeyword());
        return SchedulePaginationResponseDto.from(schedulePage);
    }
}
