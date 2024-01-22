package com.damo.server.domain.oauth.google;

import com.damo.server.domain.oauth.OAuthCodeRequestUrlProvider;
import com.damo.server.domain.oauth.OAuthProviderType;
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
                // TODO: UriString 수정
                .fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", googleOAuthConfig.clientId())
                .queryParam("redirect_uri", googleOAuthConfig.redirectUri())
                .queryParam("scope", String.join(",", googleOAuthConfig.scope()))
                .toUriString();
    }
}
