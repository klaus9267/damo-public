package com.damo.server.application.config.oauth.google;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoogleToken (
        String accessToken,
        String refreshToken,
        String tokenType,
        Integer expiresIn,
        String error,
        String errorDescription
) {
}