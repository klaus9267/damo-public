package com.damo.server.application.config.oauth.provider;

/**
 * {@code OAuthCodeRequestUrlProvider} 인터페이스는 OAuth 코드 요청 URL을 제공하는 역할을 정의합니다.
 * 구현체들은 각각의 OAuth 공급자에 특화된 코드 요청 URL을 생성하는 역할을 수행합니다.
 * {@code checkstyle:AbbreviationAsWordInName} 경고를 무시하는 애너테이션을 사용하였습니다.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public interface OAuthCodeRequestUrlProvider {
  OAuthProviderType providerType();

  String provide(final boolean isDev);
}
