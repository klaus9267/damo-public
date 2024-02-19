package com.damo.server.domain.transaction.service;

import com.damo.server.application.security.user_details.SecurityUserUtil;
import com.damo.server.domain.common.exception.ExceptionThrowHelper;
import com.damo.server.domain.common.pagination.param.TransactionPaginationParam;
import com.damo.server.domain.transaction.TransactionRepository;
import com.damo.server.domain.transaction.dto.TransactionPaginationResponseDto;
import com.damo.server.domain.transaction.dto.TransactionTotalAmountDto;
import com.damo.server.domain.transaction.dto.TransactionWithScheduleDto;
import com.damo.server.domain.transaction.entity.Transaction;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


/**
 * `TransactionReadService`는 주어진 조건에 따라 거래 내역 및 거래 총액을 조회하는 서비스를 제공합니다.
 */
@AllArgsConstructor
@Service
public class TransactionReadService {
  private final TransactionRepository transactionRepository;
  private final SecurityUserUtil securityUserUtil;
  
  /**
   * 사용자 ID에 따라 거래 총액을 조회 후 결과를 반환합니다.
   *
   * @return 조회된 거래 총액
   */
  public TransactionTotalAmountDto readTotalAmounts() {
    return transactionRepository.findTotalAmount(securityUserUtil.getId());
  }
  
  /**
   * 사용자 ID와 조회 시작 날짜에 따라 거래 총액을 조회 후 결과를 반환합니다.
   *
   * @param startedAt 조회 시작 날짜
   * @return 조회딘 거래 총액
   */
  public TransactionTotalAmountDto readRecentAmounts(final LocalDateTime startedAt) {
    final LocalDateTime startDate = startedAt == null
        ? LocalDateTime.now().minusMonths(1)
        : startedAt;
    return transactionRepository.readRecentAmounts(securityUserUtil.getId(), startDate);
  }
  
  /**
   * 사용자 ID와 거래 내역 ID에 따라 내역을 조회하고, 조회 결과를 응답 DTO로 반홥합니다.
   *
   * @param transactionId 조회할 내역 ID
   * @return 조회된 내역, 일정을 포함하는 내역 DTO
   */
  public TransactionWithScheduleDto readTransaction(final Long transactionId) {
    final Transaction transaction = transactionRepository
        .findByIdAndUserId(transactionId, securityUserUtil.getId())
        .orElseThrow(ExceptionThrowHelper.throwNotFound("조회할 거래 내역을 찾을 수 없음"));
    return TransactionWithScheduleDto.from(transaction);
  }
  
  /**
   * 사용자 ID, 조회 시작 날짜, 조회 종료 날짜, 거래 종류에 따라 내역을 조회하고, 조회 결롸를 페이지로 반환합니다.
   *
   * @param param 조회할 내역 목록의 페이징 및 검색 조건
   * @return 조회된 내역 및 일정 정보를 포함하는 페이지 응답 DTO
   */
  public TransactionPaginationResponseDto readTransactionList(
      final TransactionPaginationParam param
  ) {
    final Page<TransactionWithScheduleDto> transactionPage = transactionRepository
        .findAllByUserId(
            param.toPageable(),
            securityUserUtil.getId(),
            param.getStartedAt(),
            param.getEndedAt(),
            param.getAction()
        );
    return TransactionPaginationResponseDto.from(transactionPage);
  }
}
