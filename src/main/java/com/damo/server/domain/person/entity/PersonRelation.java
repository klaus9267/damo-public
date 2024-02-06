package com.damo.server.domain.person.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * `PersonRelation` 열거형은 개인의 관계 유형을 정의합니다.
 * 각 관계 유형은 `key`와 `title`로 구성되어 있습니다.
 */
@Getter
@RequiredArgsConstructor
public enum PersonRelation {
  FAMILY("FAMILY", "가족"),
  RELATIVE("RELATIVE", "친척"),
  FRIEND("FRIEND", "친구"),
  ACQUAINTANCE("ACQUAINTANCE", "지인"),
  COMPANY("COMPANY", "회사"),
  ETC("ETC", "기타");

  private final String key;
  private final String title;
}
