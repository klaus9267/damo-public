package com.damo.server.domain.oauth.naver;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

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
