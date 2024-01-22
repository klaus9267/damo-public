package com.damo.server.application.config.jwt;

import lombok.Builder;

@Builder
public record JwtToken (String accessToken, String refreshToken) {}
