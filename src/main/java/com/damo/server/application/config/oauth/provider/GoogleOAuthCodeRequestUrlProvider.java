package com.damo.server.application.config.oauth.provider;

import com.damo.server.application.config.oauth.config.GoogleOAuthConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class GoogleOAuthCodeRequestUrlProvider implements OAuthCodeRequestUrlProvider {
    private final GoogleOAuthConfig googleOAuthConfig;

    @Value("${oauth.is-back}")
    private boolean isBack;

    @Override
    public OAuthProviderType providerType() {
        return OAuthProviderType.GOOGLE;
    }

    @Override
    public String provide(final boolean isDev) {
        final String redirectUri = isBack
                ? googleOAuthConfig.backRedirectUri()
                : isDev
                ? googleOAuthConfig.devRedirectUri()
                : googleOAuthConfig.redirectUri();
        return UriComponentsBuilder
                .fromUriString("https://accounts.google.com/o/oauth2/auth")
                .queryParam("response_type", "code")
                .queryParam("client_id", googleOAuthConfig.clientId())
                .queryParam("redirect_uri", redirectUri)
                .queryParam("scope", String.join(" ", googleOAuthConfig.scope()))
                .toUriString();
    }
}
