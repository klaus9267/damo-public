package com.damo.server.application.security;

import com.damo.server.application.security.provider.OAuthProviderType;
import com.damo.server.domain.common.exception.ExceptionThrowHelper;
import com.damo.server.domain.user.entity.User;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * {@code OAuthUserClientComposite} 클래스는 다양한 OAuth 공급자에 대한 클라이언트들을 관리하고,
 * 해당 공급자에 따라 사용자 정보를 가져오기 위한 합성 클래스입니다.
 */
@SuppressWarnings({"checkstyle:AbbreviationAsWordInName", "checkstyle:ParameterName"})
@Component
public class OAuthUserClientComposite {
  private final Map<OAuthProviderType, OAuthUserClient> map;

  public OAuthUserClientComposite(final Set<OAuthUserClient> clients) {
    this.map = clients.stream().collect(Collectors.toMap(OAuthUserClient::providerType, Function.identity()));
  }

  public User fetch(final OAuthProviderType oAuthProviderType, final String authCode, final boolean isDev) {
    return getClient(oAuthProviderType).fetch(authCode, isDev);
  }

  private OAuthUserClient getClient(OAuthProviderType oAuthProviderType) {
    return Optional.ofNullable(map.get(oAuthProviderType))
        .orElseThrow(ExceptionThrowHelper.throwUnauthorized("지원하지 않는 소셜 로그인"));
  }
}
