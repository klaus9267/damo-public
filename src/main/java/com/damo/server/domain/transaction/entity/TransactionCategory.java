package com.damo.server.domain.transaction.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * `TransactionCategory` 열거형은 내역의 거래 자산을 정의합니다.
 * 각 관계 유형은 `key`와 `title`로 구성되어 있습니다.
 */
@Getter
@RequiredArgsConstructor
public enum TransactionCategory {
  CASH("CASH", "현금"),
  GIFT("GIFT", "선물"),
  MOBILE_GIFTS("MOBILE_GIFTS", "기프티콘"),
  ETC("ETC", "기타");
  
  private final String key;
  private final String title;
}
