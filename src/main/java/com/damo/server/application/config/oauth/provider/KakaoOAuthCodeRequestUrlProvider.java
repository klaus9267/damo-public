package com.damo.server.application.config.oauth.provider;

import com.damo.server.application.config.oauth.config.KakaoOAuthConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KakaoOAuthCodeRequestUrlProvider implements OAuthCodeRequestUrlProvider {
    private final KakaoOAuthConfig kakaoOAuthConfig;

    @Override
    public OAuthProviderType providerType() {
        return OAuthProviderType.KAKAO;
    }

    @Override
    public String provide(final boolean isDev) {
        return UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", kakaoOAuthConfig.clientId())
                .queryParam("redirect_uri", isDev ? kakaoOAuthConfig.devRedirectUri() : kakaoOAuthConfig.redirectUri())
                .queryParam("scope", String.join(",", kakaoOAuthConfig.scope()))
                .toUriString();
    }
}
