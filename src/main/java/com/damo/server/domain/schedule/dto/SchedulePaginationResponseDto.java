package com.damo.server.domain.schedule.dto;

import com.damo.server.domain.common.pagination.dto.PaginationResponseDto;
import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Page;

/**
 * {@code SchedulePaginationResponseDto}는 페이지네이션된 일정 목록의 응답 DTO입니다.
 */
@Getter
public class SchedulePaginationResponseDto
    implements PaginationResponseDto<ScheduleWithTransactionDto> {
  private final Integer totalPages;
  private final Long totalElements;
  private final Boolean isFirst;
  private final Boolean isLast;
  private final Boolean hasNext;
  private final List<ScheduleWithTransactionDto> items;
  
  /**
   * 내부 생성자로 페이지 객체를 받아 필드를 초기화합니다.
   */
  private SchedulePaginationResponseDto(final Page<ScheduleWithTransactionDto> schedulePage) {
    this.totalPages = schedulePage.getTotalPages();
    this.totalElements = schedulePage.getTotalElements();
    this.isFirst = schedulePage.isFirst();
    this.isLast = schedulePage.isLast();
    this.hasNext = schedulePage.hasNext();
    this.items = schedulePage.getContent();
  }
  
  /**
   * 주어진 페이지 객체로부터 {@code SchedulePaginationResponseDto}를 생성합니다.
   */
  public static SchedulePaginationResponseDto from(final Page<ScheduleWithTransactionDto> schedulePage) {
    return new SchedulePaginationResponseDto(schedulePage);
  }
}
