package com.damo.server.application.config.new_oauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.kakao")
public record KakaoOAuthConfig (
        String redirectUri,
        String clientId,
        String clientSecret,
        String[] scope
) {
}
