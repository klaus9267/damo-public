package com.damo.server.application.config.oauth.kakao;

import com.damo.server.domain.oauth.OAuthMember;
import com.damo.server.application.config.oauth.OAuthMemberClient;
import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import com.damo.server.application.config.oauth.config.KakaoOAuthConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class KakaoMemberClient implements OAuthMemberClient {
    private final KakaoApiClient kakaoApiClient;
    private final KakaoOAuthConfig kakaoOAuthConfig;

    @Override
    public OAuthProviderType providerType() {
        return OAuthProviderType.KAKAO;
    }

    @Override
    public OAuthMember fetch(final String authCode) {
        final KakaoToken kakaoToken = kakaoApiClient.fetchToken(getTokenRequestParam(authCode));
        final KakaoMemberResponse kakaoMemberResponse = kakaoApiClient.fetchMember("Bearer " + kakaoToken.accessToken());
        return kakaoMemberResponse.toDomain();  // (3)
    }

    private MultiValueMap<String, String> getTokenRequestParam(final String authCode) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoOAuthConfig.clientId());
        params.add("redirect_uri", kakaoOAuthConfig.redirectUri());
        params.add("code", authCode);
        params.add("client_secret", kakaoOAuthConfig.clientSecret());

        return params;
    }
}
