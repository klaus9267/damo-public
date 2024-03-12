package com.damo.server.domain.transaction.dto;

import com.damo.server.domain.common.pagination.dto.PaginationResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;


/**
 * {@code TransactionPaginationResponseDto}는 페이지네이션된 내역 목록의 응답 DTO입니다.
 */
@Getter
@RequiredArgsConstructor
public class TransactionPaginationResponseDto
    implements PaginationResponseDto<TransactionWithScheduleDto> {
  private final Integer totalPages;
  private final Long totalElements;
  private final Boolean isFirst;
  private final Boolean isLast;
  private final Boolean hasNext;
  private final List<TransactionWithScheduleDto> items;

  private TransactionPaginationResponseDto(final Page<TransactionWithScheduleDto> transactionPage) {
    this.totalPages = transactionPage.getTotalPages();
    this.totalElements = transactionPage.getTotalElements();
    this.isFirst = transactionPage.isFirst();
    this.isLast = transactionPage.isLast();
    this.hasNext = transactionPage.hasNext();
    this.items = transactionPage.getContent();
  }

  public static TransactionPaginationResponseDto from(
      final Page<TransactionWithScheduleDto> transactionPage
  ) {
    return new TransactionPaginationResponseDto(transactionPage);
  }
}
