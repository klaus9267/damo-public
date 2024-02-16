package com.damo.server.domain.schedule.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * {@code SchedulePaginationResponseDto}는 페이지네이션된 일정 목록의 응답 DTO입니다.
 */
@Getter
public class SchedulePaginationResponseDto {
  private final Integer totalPages;
  private final Long totalElements;
  private final List<ScheduleWithTransactionDto> schedules;
  
  /**
   * 내부 생성자로 페이지 객체를 받아 필드를 초기화합니다.
   */
  private SchedulePaginationResponseDto(final Page<ScheduleWithTransactionDto> schedulePage) {
    this.totalPages = schedulePage.getTotalPages();
    this.totalElements = schedulePage.getTotalElements();
    this.schedules = schedulePage.getContent();
  }
  
  /**
   * 주어진 페이지 객체로부터 {@code SchedulePaginationResponseDto}를 생성합니다.
   */
  public static SchedulePaginationResponseDto from(final Page<ScheduleWithTransactionDto> schedulePage) {
    return new SchedulePaginationResponseDto(schedulePage);
  }
}
