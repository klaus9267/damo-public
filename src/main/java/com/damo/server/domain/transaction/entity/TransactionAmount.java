package com.damo.server.domain.transaction.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * `TransactionAmount` 클래스는 시스템에서 내역에 포함되어 거래 금액을 나타냅니다
 * 이 클래스는 데이터베이스의 "transactions" 테이블에 내장되있습니다.
 */
@Getter
@Embeddable
@NoArgsConstructor(force = true)
@Builder
@RequiredArgsConstructor
public class TransactionAmount {
  private static final Long DEFAULT_AMOUNT = 0L;
  
  @Enumerated(EnumType.STRING)
  private final TransactionAction action;
  
  private final Long amount;
  
  /**
   * 'TransactionRepository'에서 금액 조회할 때  시용되는 생성자
   *
   * @param amount 거래 총액
   * @param action 거래 종류
   */
  public TransactionAmount(Long amount, String action) {
    this.amount = amount == null ? DEFAULT_AMOUNT : amount;
    this.action = TransactionAction.valueOf(action);
  }
}
