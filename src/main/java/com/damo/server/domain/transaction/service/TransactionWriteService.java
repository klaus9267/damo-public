package com.damo.server.domain.transaction.service;

import com.damo.server.application.config.user_details.SecurityUserUtil;
import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import com.damo.server.domain.transaction.TransactionRepository;
import com.damo.server.domain.transaction.dto.RequestCreateTransactionDto;
import com.damo.server.domain.transaction.dto.RequestUpdateTransactionDto;
import com.damo.server.domain.transaction.entity.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * * `TransactionWriteService`는 내역을 추가, 수정, 삭제하는 서비스를 제공합니다.
 */
@AllArgsConstructor
@Service
public class TransactionWriteService {
  private final TransactionRepository transactionRepository;
  private final SecurityUserUtil securityUserUtil;
  
  /**
   * 새로운 내역, 일정을 추가합니다
   *
   * @param transactionDto 추가할 내역 정보를 담은 DTO
   */
  @Transactional
  public void save(final RequestCreateTransactionDto transactionDto) {
    if (transactionRepository.existsByEventDateAndPersonIdAndEvent(transactionDto.eventDate(), transactionDto.personId(), transactionDto.event())) {
      throw new CustomException(CustomErrorCode.BAD_REQUEST, "내역에서 동일한 기록이 존재");
    }
    transactionRepository.save(Transaction.from(transactionDto, securityUserUtil.getId()));
  }
  
  /**
   * 지정된 내역 ID에 해당하는 내역을 수정합니다.
   *
   * @param transactionDto 수정할 내역 정보를 담은 DTO
   * @param transactionId  수정할 내역의 ID
   */
  @Transactional
  public void patchTransactionById(final RequestUpdateTransactionDto transactionDto, final Long transactionId) {
    final Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, securityUserUtil.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND, "수정할 내역을 찾을 수 없음"));
    transaction.changeInfo(transactionDto);
  }
  
  /**
   * 지정된 내역 ID에 해당하는 내역을 삭제합니다.
   *
   * @param transactionId 삭제할 내역의 ID
   */
  @Transactional
  public void removeTransactionById(final Long transactionId) {
    transactionRepository.findByIdAndUserId(transactionId, securityUserUtil.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND, "삭제할 내역을 찾을 수 없음"));
    transactionRepository.deleteById(transactionId);
  }
}
