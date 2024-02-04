package com.damo.server.application.config.oauth.provider;

import com.damo.server.application.config.oauth.config.NaverOAuthConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * {@code NaverOAuthCodeRequestUrlProvider} 클래스는 Naver OAuth의 코드 요청 URL을 제공하는 컴포넌트입니다.
 * {@code OAuthCodeRequestUrlProvider} 인터페이스를 구현하며, Naver OAuth에 특화된 설정을 이용하여
 * 코드 요청 URL을 생성합니다.
 * {@code checkstyle:AbbreviationAsWordInName} 경고를 무시하는 애너테이션을 사용하였습니다.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@Component
@RequiredArgsConstructor
public class NaverOAuthCodeRequestUrlProvider implements OAuthCodeRequestUrlProvider {
  private final NaverOAuthConfig naverOAuthConfig;

  @Value("${oauth.is-back}")
  private boolean isBack;

  @Override
  public OAuthProviderType providerType() {
    return OAuthProviderType.NAVER;
  }

  @Override
  public String provide(final boolean isDev) {
    String redirectUri;
    if (isBack) {
      redirectUri = naverOAuthConfig.backRedirectUri();
    } else if (isDev) {
      redirectUri = naverOAuthConfig.devRedirectUri();
    } else {
      redirectUri = naverOAuthConfig.redirectUri();
    }
    return UriComponentsBuilder
        .fromUriString("https://nid.naver.com/oauth2.0/authorize")
        .queryParam("response_type", "code")
        .queryParam("client_id", naverOAuthConfig.clientId())
        .queryParam("redirect_uri", redirectUri)
        .queryParam("state", "samplestate") // 이건 나중에 따로 찾아보고 설정해서 쓰세용!
        .build()
        .toUriString();
  }
}
