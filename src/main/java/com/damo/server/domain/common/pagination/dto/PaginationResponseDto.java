package com.damo.server.domain.common.pagination.dto;

import java.util.List;

/**
 * 페이지네이션 응답 타입을 통일하기 위한 인터페이스.
 */
public interface PaginationResponseDto<T> {
  Integer getTotalPages();

  Long getTotalElements();

  List<T> getItems();
}