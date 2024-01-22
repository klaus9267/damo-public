package com.damo.server.domain.oauth.naver;

import com.damo.server.domain.oauth.OAuthMember;
import com.damo.server.domain.oauth.OAuthMemberClient;
import com.damo.server.domain.oauth.OAuthProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class NaverMemberClient implements OAuthMemberClient {
    private final NaverApiClient naverApiClient;
    private final NaverOAuthConfig naverOAuthConfig;

    @Override
    public OAuthProviderType providerType() {
        return OAuthProviderType.NAVER;
    }
    @Override
    public OAuthMember fetch(final String authCode) {
        final NaverToken tokenInfo = naverApiClient.fetchToken(tokenRequestParams(authCode));
        final NaverMemberResponse naverMemberResponse = naverApiClient.fetchMember("Bearer " + tokenInfo.accessToken());
        return naverMemberResponse.toDomain();
    }

    private MultiValueMap<String, String> tokenRequestParams(final String authCode) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("grant_type", "authorization_code");
        params.add("client_id", naverOAuthConfig.clientId());
        params.add("client_secret", naverOAuthConfig.clientSecret());
        params.add("code", authCode);
        params.add("state", naverOAuthConfig.state());

        return params;
    }
}
