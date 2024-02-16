package com.damo.server.application.config.oauth.config;

import java.util.Arrays;
import java.util.Objects;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Naver OAuth 설정을 나타내는 Immutable(불변) 레코드 클래스입니다.
 * {@code ConfigurationProperties} 애너테이션을 통해 프로퍼티 값들을 읽어오며, 프로퍼티의 prefix는 "oauth.naver" 입니다.
 * 대상 필드들은 {@code toString}, {@code equals}, {@code hashCode} 메서드에서 고려됩니다.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@ConfigurationProperties(prefix = "oauth.naver")
public record NaverOAuthConfig(
    String redirectUri,
    String devRedirectUri,
    String backRedirectUri,
    String clientId,
    String clientSecret,
    String[] scope,
    String state
) {
  @Override
  public String toString() {
    return "NaverOAuthConfig{"
      + "redirectUri='" + redirectUri + '\''
      + ", devRedirectUri='" + devRedirectUri + '\''
      + ", backRedirectUri='" + backRedirectUri + '\''
      + ", clientId='" + clientId + '\''
      + ", clientSecret='" + clientSecret + '\''
      + ", scope=" + Arrays.toString(scope)
      + ", state='" + state + '\''
      + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final NaverOAuthConfig that = (NaverOAuthConfig) o;
    return Objects.equals(redirectUri, that.redirectUri)
      && Objects.equals(devRedirectUri, that.devRedirectUri)
      && Objects.equals(backRedirectUri, that.backRedirectUri)
      && Objects.equals(clientId, that.clientId)
      && Objects.equals(clientSecret, that.clientSecret)
      && Arrays.equals(scope, that.scope)
      && Objects.equals(state, that.state);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(
        redirectUri,
        devRedirectUri,
        backRedirectUri,
        clientId,
        clientSecret,
        state
    );
    result = 31 * result + Arrays.hashCode(scope);
    return result;
  }
}
