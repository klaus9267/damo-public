package com.damo.server.domain.oauth.naver;

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
