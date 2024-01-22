package com.damo.server.application.config.oauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.naver")
public record NaverOAuthConfig (
        String redirectUri,
        String clientId,
        String clientSecret,
        String[] scope,
        String state
) {
}
