package com.damo.server.domain.oauth.google;

import com.damo.server.domain.oauth.OAuthMember;
import com.damo.server.domain.oauth.OAuthMemberClient;
import com.damo.server.domain.oauth.OAuthProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class GoogleMemberClient implements OAuthMemberClient {
    private final GoogleApiClient googleApiClient;
    private final GoogleOAuthConfig googleOAuthConfig;

    @Override
    public OAuthProviderType providerType() {
        return OAuthProviderType.GOOGLE;
    }

    @Override
    public OAuthMember fetch(final String authCode) {
        final GoogleToken tokenInfo = googleApiClient.fetchToken(tokenRequestParams(authCode));
        final GoogleMemberResponse googleMemberResponse = googleApiClient.fetchMember("Bearer " + tokenInfo.accessToken());
        return googleMemberResponse.toDomain();
    }

    private MultiValueMap<String, String> tokenRequestParams(final String authCode) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("grant_type", "authorization_code");
        params.add("client_id", googleOAuthConfig.clientId());
        params.add("client_secret", googleOAuthConfig.clientSecret());
        params.add("redirect_uri", googleOAuthConfig.redirectUri());
        params.add("code", authCode);

        return params;
    }
}
