package com.damo.server.domain.common.pagination;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * {@code PaginationSortGroup}은 페이지네이션에서 사용되는 정렬 그룹을 정의하는 열거형입니다.
 * 각 정렬 그룹에는 허용된 정렬 유형 목록이 포함되어 있습니다.
 */
@AllArgsConstructor
@Getter
public enum PaginationSortGroup {
  PERSON(List.of(PaginationSortType.NAME, PaginationSortType.RELATION, PaginationSortType.CREATED_AT)),
  TRANSACTION(List.of(PaginationSortType.ACTION, PaginationSortType.SCHEDULE_EVENT_DATE)),
  SCHEDULE(List.of(PaginationSortType.EVENT_DATE)),
  EMPTY(List.of(PaginationSortType.EMPTY));

  /**
   * 정렬 그룹에 속하는 허용된 정렬 유형 목록.
   */
  private final List<PaginationSortType> sortTypes;
}
