package com.damo.server.domain.person.dto;

import com.damo.server.domain.common.pagination.dto.PaginationResponseDto;
import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Page;

/**
 * {@code PeoplePaginationResponseDto}는 페이지네이션된 대상 목록의 응답 DTO입니다.
 */
@Getter
public class PeoplePaginationResponseDto
    implements PaginationResponseDto<PeopleWithTransactionCountDto> {
  private final Integer totalPages;
  private final Long totalElements;
  private final List<PeopleWithTransactionCountDto> items;

  private PeoplePaginationResponseDto(final Page<PeopleWithTransactionCountDto> peoplePage) {
    this.totalPages = peoplePage.getTotalPages();
    this.totalElements = peoplePage.getTotalElements();
    this.items = peoplePage.getContent();
  }

  public static PeoplePaginationResponseDto from(
      final Page<PeopleWithTransactionCountDto> peoplePage
  ) {
    return new PeoplePaginationResponseDto(peoplePage);
  }
}
