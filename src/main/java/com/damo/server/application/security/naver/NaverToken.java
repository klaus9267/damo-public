package com.damo.server.application.security.naver;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Naver OAuth에서 받은 토큰 정보를 담는 Immutable(불변) 레코드 클래스입니다.
 * {@code JsonNaming} 애너테이션을 통해 JSON 직렬화 시에 속성 이름을 Snake Case로 변환합니다.
 */
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record NaverToken(
    String accessToken,
    String refreshToken,
    String tokenType,
    Integer expiresIn,
    String error,
    String errorDescription
) {
}
