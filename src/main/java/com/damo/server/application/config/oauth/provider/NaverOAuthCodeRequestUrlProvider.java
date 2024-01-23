package com.damo.server.application.config.oauth.provider;

import com.damo.server.application.config.oauth.config.NaverOAuthConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class NaverOAuthCodeRequestUrlProvider implements OAuthCodeRequestUrlProvider {
    private final NaverOAuthConfig naverOAuthConfig;

    @Override
    public OAuthProviderType providerType() {
        return OAuthProviderType.NAVER;
    }

    @Override
    public String provide(final boolean isDev) {
        return UriComponentsBuilder
                .fromUriString("https://nid.naver.com/oauth2.0/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", naverOAuthConfig.clientId())
                .queryParam("redirect_uri", isDev ? naverOAuthConfig.devRedirectUri() : naverOAuthConfig.redirectUri())
                .queryParam("state", "samplestate") // 이건 나중에 따로 찾아보고 설정해서 쓰세용!
                .build()
                .toUriString();
    }
}
