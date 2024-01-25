package com.damo.server.application.config.jwt;

import lombok.Builder;

/**
 * JWT 토큰의 액세스 토큰과 리프레시 토큰을 나타냅니다.
 */
@Builder
public record JwtToken(String accessToken, String refreshToken) {}
