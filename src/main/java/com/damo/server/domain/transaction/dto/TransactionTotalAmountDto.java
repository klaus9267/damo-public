package com.damo.server.domain.transaction.dto;

import com.damo.server.domain.transaction.entity.TransactionAmount;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * {@code TransactionTotalAmountDto}는 거래 내역 총액을 나타내는 클래스입니다.
 */
@Getter
@AllArgsConstructor
public class TransactionTotalAmountDto {
  private final Long totalGivingAmount;
  private final Long totalReceivingAmount;
  
  public TransactionTotalAmountDto(final TransactionAmount totalGivingAmount, final TransactionAmount totalReceivingAmount) {
    this.totalReceivingAmount = totalReceivingAmount.getAmount();
    this.totalGivingAmount = totalGivingAmount.getAmount();
  }
}
