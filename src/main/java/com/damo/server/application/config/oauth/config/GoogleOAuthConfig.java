package com.damo.server.application.config.oauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.google")
public record GoogleOAuthConfig (
        String redirectUri,
        String devRedirectUri,
        String backRedirectUri,
        String clientId,
        String clientSecret,
        String[] scope
) {
}
