package com.damo.server.application.security.provider;

import com.damo.server.application.config.oauth.GoogleOAuthConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * {@code GoogleOAuthCodeRequestUrlProvider} 클래스는 Google OAuth의 코드 요청 URL을 제공하는 컴포넌트입니다.
 * {@code OAuthCodeRequestUrlProvider} 인터페이스를 구현하며, Google OAuth에 특화된 설정을 이용하여
 * 코드 요청 URL을 생성합니다.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@Component
@RequiredArgsConstructor
public class GoogleOAuthCodeRequestUrlProvider implements OAuthCodeRequestUrlProvider {
  private final GoogleOAuthConfig googleOAuthConfig;

  @Value("${oauth.is-back}")
  private boolean isBack;

  @Override
  public OAuthProviderType providerType() {
    return OAuthProviderType.GOOGLE;
  }

  @Override
  public String provide(final boolean isDev) {
    String redirectUri;
    if (isBack) {
      redirectUri = googleOAuthConfig.backRedirectUri();
    } else if (isDev) {
      redirectUri = googleOAuthConfig.devRedirectUri();
    } else {
      redirectUri = googleOAuthConfig.redirectUri();
    }
    return UriComponentsBuilder
        .fromUriString("https://accounts.google.com/o/oauth2/auth")
        .queryParam("response_type", "code")
        .queryParam("client_id", googleOAuthConfig.clientId())
        .queryParam("redirect_uri", redirectUri)
        .queryParam("scope", String.join(" ", googleOAuthConfig.scope()))
        .toUriString();
  }
}
