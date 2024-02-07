package com.damo.server.domain.transaction.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * `TransactionAction` 열거형은 내역의 거래 유형을 정의합니다.
 * 각 관계 유형은 `key`와 `title`로 구성되어 있습니다.
 */
@Getter
@RequiredArgsConstructor
public enum TransactionAction {
  TOTAL("TOTAL", "전체"),
  GIVING("GIVING", "준"),
  RECEIVING("RECEIVING", "받은");
  
  private final String key;
  private final String title;
}
