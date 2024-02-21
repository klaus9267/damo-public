package com.damo.server.application.security.provider;

import com.damo.server.domain.common.exception.ExceptionThrowHelper;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * {@code OAuthCodeRequestUrlProviderComposite} 클래스는
 * 다수의 {@code OAuthCodeRequestUrlProvider}를 조합하여 사용하는 역할을 합니다.
 * 각각의 OAuth 공급자에 대한 코드 요청 URL을 제공하는 역할을 수행합니다.
 */
@SuppressWarnings({"checkstyle:AbbreviationAsWordInName", "checkstyle:ParameterName"})
@Component
public class OAuthCodeRequestUrlProviderComposite {
  private final Map<OAuthProviderType, OAuthCodeRequestUrlProvider> map;

  /**
   * 주어진 {@code Set<OAuthCodeRequestUrlProvider>}에서 각각의 공급자에 대한 매핑을 생성하는 생성자입니다.
   *
   * @param providers OAuth 코드 요청 URL을 제공하는 여러 공급자
   */
  public OAuthCodeRequestUrlProviderComposite(final Set<OAuthCodeRequestUrlProvider> providers) {
    this.map = providers
      .stream()
      .collect(
        Collectors.toMap(
            OAuthCodeRequestUrlProvider::providerType,
            Function.identity()
          )
      );
  }

  public String provide(final OAuthProviderType oAuthProviderType, final boolean isDev) {
    return getProvider(oAuthProviderType).provide(isDev);
  }

  private OAuthCodeRequestUrlProvider getProvider(final OAuthProviderType oAuthProviderType) {
    return Optional.ofNullable(map.get(oAuthProviderType))
        .orElseThrow(ExceptionThrowHelper.throwUnauthorized("지원하지 않는 소셜 로그인"));
  }
}
