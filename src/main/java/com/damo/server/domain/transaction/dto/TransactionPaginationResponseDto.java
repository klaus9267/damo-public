package com.damo.server.domain.transaction.dto;

import com.damo.server.domain.common.pagination.dto.PaginationResponseDto;
import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Page;


/**
 * {@code TransactionPaginationResponseDto}는 페이지네이션된 내역 목록의 응답 DTO입니다.
 */
@Getter
public class TransactionPaginationResponseDto
    implements PaginationResponseDto<TransactionWithScheduleDto> {
  private final Integer totalPages;
  private final Long totalElements;
  private final List<TransactionWithScheduleDto> items;
  
  private TransactionPaginationResponseDto(final Page<TransactionWithScheduleDto> transactionPage) {
    this.totalPages = transactionPage.getTotalPages();
    this.totalElements = transactionPage.getTotalElements();
    this.items = transactionPage.getContent();
  }
  
  public static TransactionPaginationResponseDto from(
      final Page<TransactionWithScheduleDto> transactionPage
  ) {
    return new TransactionPaginationResponseDto(transactionPage);
  }
}
