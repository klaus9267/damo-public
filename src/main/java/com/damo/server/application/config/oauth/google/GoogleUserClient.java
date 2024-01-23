package com.damo.server.application.config.oauth.google;

import com.damo.server.application.config.oauth.OAuthUserClient;
import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import com.damo.server.application.config.oauth.config.GoogleOAuthConfig;
import com.damo.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class GoogleUserClient implements OAuthUserClient {
    private final GoogleApiClient googleApiClient;
    private final GoogleOAuthConfig googleOAuthConfig;

    @Override
    public OAuthProviderType providerType() {
        return OAuthProviderType.GOOGLE;
    }

    @Override
    public User fetch(final String authCode, final boolean isDev) {
        final GoogleToken tokenInfo = googleApiClient.fetchToken(tokenRequestParams(authCode, isDev));
        final GoogleUserResponse googleMemberResponse = googleApiClient.fetchMember("Bearer " + tokenInfo.accessToken());
        return googleMemberResponse.toDomain();
    }

    private MultiValueMap<String, String> tokenRequestParams(final String authCode, final boolean isDev) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("grant_type", "authorization_code");
        params.add("client_id", googleOAuthConfig.clientId());
        params.add("client_secret", googleOAuthConfig.clientSecret());
        params.add("redirect_uri", isDev ? googleOAuthConfig.devRedirectUri() : googleOAuthConfig.redirectUri());
        params.add("code", authCode);

        return params;
    }
}
