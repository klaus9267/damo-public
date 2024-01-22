package com.damo.server.application.config.oauth.provider;

import com.damo.server.application.config.oauth.config.GoogleOAuthConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class GoogleOAuthCodeRequestUrlProvider implements OAuthCodeRequestUrlProvider {
    private final GoogleOAuthConfig googleOAuthConfig;

    @Override
    public OAuthProviderType providerType() {
        return OAuthProviderType.GOOGLE;
    }

    @Override
    public String provide() {
        return UriComponentsBuilder
                .fromUriString("https://accounts.google.com/o/oauth2/auth")
                .queryParam("response_type", "code")
                .queryParam("client_id", googleOAuthConfig.clientId())
                .queryParam("redirect_uri", googleOAuthConfig.redirectUri())
                .queryParam("scope", String.join(" ", googleOAuthConfig.scope()))
                .toUriString();
    }
}
