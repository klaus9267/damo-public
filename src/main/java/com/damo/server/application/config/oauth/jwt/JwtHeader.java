package com.damo.server.application.config.oauth.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtHeader {
    AUTHORIZATION("Authorization", "access token"),
    AUTHORIZATION_REFRESH("Authorization_refresh", "refresh token"),
    CONTENT_TYPE("application/json;charset=UTF-8", "토큰 응답 타입");

    private final String key;
    private final String title;
}
