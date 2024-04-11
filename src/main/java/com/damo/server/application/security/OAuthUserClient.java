package com.damo.server.application.security;

import com.damo.server.application.security.provider.OAuthProviderType;
import com.damo.server.domain.user.entity.User;

/**
 * {@code OAuthUserClient} 인터페이스는 OAuth 공급자로부터 사용자 정보를 가져오기 위한 클라이언트를 정의합니다.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public interface OAuthUserClient {
  OAuthProviderType providerType();

  User fetch(final String code, final boolean isDev);
}
