package com.damo.server.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 사용자 역할을 정의한 Enum 클래스입니다.
 */
@Getter
@RequiredArgsConstructor
public enum UserRole {
  ADMIN("ROLE_ADMIN", "관리자"),
  USER("ROLE_USER", "일반 사용자");

  private final String key;
  private final String title;
}
