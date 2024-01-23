package com.damo.server.application.config.oauth.kakao;

import com.damo.server.application.config.oauth.OAuthUserClient;
import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import com.damo.server.application.config.oauth.config.KakaoOAuthConfig;
import com.damo.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class KakaoUserClient implements OAuthUserClient {
    private final KakaoApiClient kakaoApiClient;
    private final KakaoOAuthConfig kakaoOAuthConfig;

    @Override
    public OAuthProviderType providerType() {
        return OAuthProviderType.KAKAO;
    }

    @Override
    public User fetch(final String authCode, final boolean isDev) {
        final KakaoToken kakaoToken = kakaoApiClient.fetchToken(getTokenRequestParam(authCode, isDev));
        final KakaoUserResponse kakaoMemberResponse = kakaoApiClient.fetchMember("Bearer " + kakaoToken.accessToken());
        return kakaoMemberResponse.toDomain();
    }

    private MultiValueMap<String, String> getTokenRequestParam(final String authCode, final boolean isDev) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoOAuthConfig.clientId());
        // 카카오는 redirect_uri가 필요 없지만 구글이 필요해서 일단 형식을 맞춤
        params.add("redirect_uri", isDev ? kakaoOAuthConfig.devRedirectUri() : kakaoOAuthConfig.redirectUri());
        params.add("code", authCode);
        params.add("client_secret", kakaoOAuthConfig.clientSecret());

        return params;
    }
}
