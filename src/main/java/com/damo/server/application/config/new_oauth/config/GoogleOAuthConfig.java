package com.damo.server.application.config.new_oauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.google")
public record GoogleOAuthConfig (
        String redirectUri,
        String clientId,
        String clientSecret,
        String[] scope
) {
}
