package com.damo.server.domain.person.dto;

import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Page;

/**
 * {@code PeoplePaginationResponseDto}는 페이지네이션된 대상 목록의 응답 DTO입니다.
 */
@Getter
public class PeoplePaginationResponseDto {
  private final Integer totalPages;
  private final Long totalElements;
  private final List<PeopleWithTransactionCountDto> people;

  /**
   * 내부 생성자로 페이지 객체를 받아 필드를 초기화합니다.
   */
  private PeoplePaginationResponseDto(final Page<PeopleWithTransactionCountDto> peoplePage) {
    this.totalPages = peoplePage.getTotalPages();
    this.totalElements = peoplePage.getTotalElements();
    this.people = peoplePage.getContent();
  }

  /**
   * 주어진 페이지 객체로부터 {@code PeoplePaginationResponseDto}를 생성합니다.
   */
  public static PeoplePaginationResponseDto from(
      final Page<PeopleWithTransactionCountDto> peoplePage
  ) {
    return new PeoplePaginationResponseDto(peoplePage);
  }
}
