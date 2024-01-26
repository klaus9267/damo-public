package com.damo.server.application.config.jwt;

import lombok.Builder;
import lombok.Getter;

/**
 * JWT 토큰의 액세스 토큰과 리프레시 토큰을 나타냅니다.
 */
@Getter
@Builder
public class JwtToken {
  public static final String CONTENT_TYPE = "application/json;charset=UTF-8";
  public static final String HEADER_KEY = "Authorization";
  private final String accessToken;
  private final String refreshToken;
}
