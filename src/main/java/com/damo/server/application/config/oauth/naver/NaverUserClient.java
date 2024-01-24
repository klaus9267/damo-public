package com.damo.server.application.config.oauth.naver;

import com.damo.server.application.config.oauth.OAuthUserClient;
import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import com.damo.server.application.config.oauth.config.NaverOAuthConfig;
import com.damo.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class NaverUserClient implements OAuthUserClient {
    private final NaverApiClient naverApiClient;
    private final NaverOAuthConfig naverOAuthConfig;

    @Value("${oauth.is-back}")
    private boolean isBack;

    @Override
    public OAuthProviderType providerType() {
        return OAuthProviderType.NAVER;
    }
    @Override
    public User fetch(final String authCode, final boolean isDev) {
        final NaverToken tokenInfo = naverApiClient.fetchToken(tokenRequestParams(authCode, isDev));
        final NaverUserResponse naverMemberResponse = naverApiClient.fetchMember("Bearer " + tokenInfo.accessToken());
        return naverMemberResponse.toDomain();
    }

    private MultiValueMap<String, String> tokenRequestParams(final String authCode, final boolean isDev) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("grant_type", "authorization_code");
        params.add("client_id", naverOAuthConfig.clientId());
        params.add("client_secret", naverOAuthConfig.clientSecret());
        // 네이버는 redirect_uri가 필요 없지만 구글이 필요해서 일단 형식을 맞춤
        final String redirectUri = isBack
                ? naverOAuthConfig.backRedirectUri()
                : isDev
                ? naverOAuthConfig.devRedirectUri()
                : naverOAuthConfig.redirectUri();
        params.add("redirect_uri", redirectUri);
        params.add("code", authCode);
        params.add("state", naverOAuthConfig.state());

        return params;
    }
}
