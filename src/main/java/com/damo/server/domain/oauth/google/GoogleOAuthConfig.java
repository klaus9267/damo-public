package com.damo.server.domain.oauth.google;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.google")
public record GoogleOAuthConfig (
        String redirectUri,
        String clientId,
        String clientSecret,
        String[] scope
) {
}
