package com.damo.server.domain.transaction.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * {@code TransactionPaginationResponseDto}는 페이지네이션된 내역 목록의 응답 DTO입니다.
 */
@Getter
public class TransactionPaginationResponseDto {
  private final Integer totalPages;
  private final Long totalElements;
  private final List<TransactionWithScheduleDto> transactions;
  
  private TransactionPaginationResponseDto(final Page<TransactionWithScheduleDto> transactionPage) {
    this.totalPages = transactionPage.getTotalPages();
    this.totalElements = transactionPage.getTotalElements();
    this.transactions = transactionPage.getContent();
  }
  
  public static TransactionPaginationResponseDto from(final Page<TransactionWithScheduleDto> transactionPage) {
    return new TransactionPaginationResponseDto(transactionPage);
  }
}
