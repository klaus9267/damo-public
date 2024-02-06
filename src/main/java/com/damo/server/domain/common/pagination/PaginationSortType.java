package com.damo.server.domain.common.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Sort;

/**
 * {@code PaginationSortType}은 페이지네이션에서 사용되는 정렬 유형을 정의하는 열거형입니다.
 * 각 정렬 유형에는 해당하는 필드명이 포함되어 있습니다.
 */
@AllArgsConstructor
@Getter
public enum PaginationSortType {
  NAME("name"),
  RELATION("relation"),
  CREATED_AT("createdAt"),
  ACTION("transactionAmount.action"),
  EVENT_DATE("eventDate"),
  SCHEDULE_EVENT_DATE("schedule.eventDate"),
  EMPTY(null);

  private final String field;

  /**
   * 정렬 방향을 기반으로 Spring Data의 Sort 객체를 생성하여 반환합니다.
   *
   * @param direction 정렬 방향 (ASC 또는 DESC)
   * @return Spring Data의 Sort 객체
   */
  public Sort toSort(Sort.Direction direction) {
    return Sort.by(direction, field);
  }
}
