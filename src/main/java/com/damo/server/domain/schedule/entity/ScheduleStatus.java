package com.damo.server.domain.schedule.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * `ScheduleStatus` 열거형은 일정의 중요도을 정의합니다.
 * 각 관계 유형은 `key`와 `title`로 구성되어 있습니다.
 */
@Getter
@RequiredArgsConstructor
public enum ScheduleStatus {
  IMPORTANT("IMPORTANT", "중요"),
  NORMAL("NORMAL", "일반");
  
  private final String key;
  private final String title;
}
