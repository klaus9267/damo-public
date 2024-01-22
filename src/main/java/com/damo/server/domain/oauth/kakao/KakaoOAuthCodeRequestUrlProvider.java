package com.damo.server.domain.oauth.kakao;

import com.damo.server.domain.oauth.OAuthCodeRequestUrlProvider;
import com.damo.server.domain.oauth.OAuthProviderType;
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
    public String provide() {
        return UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", kakaoOAuthConfig.clientId())
                .queryParam("redirect_uri", kakaoOAuthConfig.redirectUri())
                .queryParam("scope", String.join(",", kakaoOAuthConfig.scope()))
                .toUriString();
    }
}
