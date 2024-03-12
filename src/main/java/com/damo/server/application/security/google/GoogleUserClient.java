package com.damo.server.application.security.google;

import com.damo.server.application.security.OAuthUserClient;
import com.damo.server.application.config.oauth.GoogleOAuthConfig;
import com.damo.server.application.security.provider.OAuthProviderType;
import com.damo.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Google OAuth 사용자 정보 및 토큰을 가져오기 위한 클라이언트 구현체입니다.
 * {@code OAuthUserClient} 인터페이스를 구현하며, {@code GoogleApiClient}와 {@code GoogleOAuthConfig}을 사용합니다.
 */
@Component
@RequiredArgsConstructor
public class GoogleUserClient implements OAuthUserClient {
  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  private final GoogleOAuthConfig googleOAuthConfig;
  private final GoogleApiClient googleApiClient;

  @Value("${oauth.is-back}")
  private boolean isBack;

  @Override
  public OAuthProviderType providerType() {
    return OAuthProviderType.GOOGLE;
  }

  @Override
  public User fetch(final String authCode, final boolean isDev) {
    final GoogleToken tokenInfo = googleApiClient.fetchToken(tokenRequestParams(authCode, isDev));
    final GoogleUserResponse googleMemberResponse = googleApiClient
        .fetchMember("Bearer " + tokenInfo.accessToken());

    return googleMemberResponse.toDomain();
  }

  private MultiValueMap<String, String> tokenRequestParams(final String authCode, final boolean isDev) {
    final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    params.add("grant_type", "authorization_code");
    params.add("client_id", googleOAuthConfig.clientId());
    params.add("client_secret", googleOAuthConfig.clientSecret());

    String redirectUri;
    if (isBack) {
      redirectUri = googleOAuthConfig.backRedirectUri();
    } else if (isDev) {
      redirectUri = googleOAuthConfig.devRedirectUri();
    } else {
      redirectUri = googleOAuthConfig.redirectUri();
    }

    params.add("redirect_uri", redirectUri);
    params.add("code", authCode);
    return params;
  }
}
