package com.damo.server.application.config.oauth.provider;

import com.damo.server.application.config.oauth.config.KakaoOAuthConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * {@code KakaoOAuthCodeRequestUrlProvider} 클래스는 Kakao OAuth의 코드 요청 URL을 제공하는 컴포넌트입니다.
 * {@code OAuthCodeRequestUrlProvider} 인터페이스를 구현하며, Kakao OAuth에 특화된 설정을 이용하여
 * 코드 요청 URL을 생성합니다.
 * {@code checkstyle:AbbreviationAsWordInName} 경고를 무시하는 애너테이션을 사용하였습니다.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@Component
@RequiredArgsConstructor
public class KakaoOAuthCodeRequestUrlProvider implements OAuthCodeRequestUrlProvider {
  private final KakaoOAuthConfig kakaoOAuthConfig;

  @Value("${oauth.is-back}")
  private boolean isBack;

  @Override
  public OAuthProviderType providerType() {
    return OAuthProviderType.KAKAO;
  }

  @Override
  public String provide(final boolean isDev) {
    String redirectUri;
    if (isBack) {
      redirectUri = kakaoOAuthConfig.backRedirectUri();
    } else if (isDev) {
      redirectUri = kakaoOAuthConfig.devRedirectUri();
    } else {
      redirectUri = kakaoOAuthConfig.redirectUri();
    }
    return UriComponentsBuilder
        .fromUriString("https://kauth.kakao.com/oauth/authorize")
        .queryParam("response_type", "code")
        .queryParam("client_id", kakaoOAuthConfig.clientId())
        .queryParam("redirect_uri", redirectUri)
        .queryParam("scope", String.join(",", kakaoOAuthConfig.scope()))
        .toUriString();
  }
}
