package com.damo.server.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    ADMIN("ROLE_ADMON", "관리자"),
    USER("ROLE_USER", "일반 사용자");

    private final String key;
    private final String title;
}
