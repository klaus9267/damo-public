package com.damo.server.application.security.naver;

import com.damo.server.application.security.OAuthUserClient;
import com.damo.server.application.config.oauth.NaverOAuthConfig;
import com.damo.server.application.security.provider.OAuthProviderType;
import com.damo.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * 네이버 OAuth 사용자 정보를 가져오기 위한 클라이언트 클래스입니다.
 * {@code Component} 애너테이션을 통해 스프링에 의해 빈으로 등록되며,
 * {@code RequiredArgsConstructor} 애너테이션을 통해 생성자 주입을 지원합니다.
 */
@Component
@RequiredArgsConstructor
public class NaverUserClient implements OAuthUserClient {
  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  private final NaverOAuthConfig naverOAuthConfig;
  private final NaverApiClient naverApiClient;

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
    String redirectUri;
    if (isBack) {
      redirectUri = naverOAuthConfig.backRedirectUri();
    } else if (isDev) {
      redirectUri = naverOAuthConfig.devRedirectUri();
    } else {
      redirectUri = naverOAuthConfig.redirectUri();
    }
    params.add("redirect_uri", redirectUri);
    params.add("code", authCode);
    params.add("state", naverOAuthConfig.state());

    return params;
  }
}
