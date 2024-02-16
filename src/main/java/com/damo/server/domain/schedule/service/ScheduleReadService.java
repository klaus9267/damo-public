package com.damo.server.domain.schedule.service;

import com.damo.server.application.config.user_details.SecurityUserUtil;
import com.damo.server.domain.common.exception.ExceptionThrowHelper;
import com.damo.server.domain.common.pagination.param.SchedulePaginationParam;
import com.damo.server.domain.schedule.ScheduleRepository;
import com.damo.server.domain.schedule.dto.SchedulePaginationResponseDto;
import com.damo.server.domain.schedule.dto.ScheduleWithTransactionDto;
import com.damo.server.domain.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * `ScheduleReadService`는 주어진 조건에 따라 일정을 조회하는 서비스를 제공합니다.
 */
@AllArgsConstructor
@Service
public class ScheduleReadService {
  private final ScheduleRepository scheduleRepository;
  private final SecurityUserUtil securityUserUtil;
  
  /**
   * 일정 ID에 따라 일정을 조회 후 결과를 반환합니다.
   *
   * @param scheduleId 죄회할 일정 ID
   * @return 조회된 일정, 내역을 포함하는 일정 DTO
   */
  public ScheduleWithTransactionDto readSchedule(final Long scheduleId) {
    final Schedule schedule = scheduleRepository.findByIdAndUserId(scheduleId, securityUserUtil.getId()).orElseThrow(ExceptionThrowHelper.throwNotFound("조회할 일정을 찾을 수 없음"));
    return ScheduleWithTransactionDto.from(schedule);
  }
  
  /**
   * 사용자 ID와 기간에 따라 일정을 조회하고, 조회 결과를 페이지로 반환합니다.
   *
   * @param paginationParam 조회할 일정 목록의 페이징 및 검색 조건
   * @return 조회된 일정을 포함하는 페이지 응답 DTO
   */
  public SchedulePaginationResponseDto readScheduleByEventDate(final SchedulePaginationParam paginationParam) {
    final Page<ScheduleWithTransactionDto> schedulePage = scheduleRepository.findAllScheduleByEventDate(paginationParam.toPageable(), securityUserUtil.getId(), paginationParam.getYear(), paginationParam.getMonth(), paginationParam.getKeyword());
    return SchedulePaginationResponseDto.from(schedulePage);
  }
}
