package com.damo.server.application.config.oauth;

import java.util.Arrays;
import java.util.Objects;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Kakao OAuth 설정을 나타내는 Immutable(불변) 레코드 클래스입니다.
 * {@code ConfigurationProperties} 애너테이션을 통해 프로퍼티 값들을 읽어오며, 프로퍼티의 prefix는 "oauth.kakao" 입니다.
 * 대상 필드들은 {@code toString}, {@code equals}, {@code hashCode} 메서드에서 고려됩니다.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@ConfigurationProperties(prefix = "oauth.kakao")
public record KakaoOAuthConfig(
    String redirectUri,
    String devRedirectUri,
    String backRedirectUri,
    String clientId,
    String clientSecret,
    String[] scope
) {
  @Override
  public String toString() {
    return "KakaoOAuthConfig{"
      + "redirectUri='" + redirectUri + '\''
      + ", devRedirectUri='" + devRedirectUri + '\''
      + ", backRedirectUri='" + backRedirectUri + '\''
      + ", clientId='" + clientId + '\''
      + ", clientSecret='" + clientSecret + '\''
      + ", scope=" + Arrays.toString(scope)
      + '}';
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final KakaoOAuthConfig that = (KakaoOAuthConfig) o;
    return Objects.equals(redirectUri, that.redirectUri)
      && Objects.equals(devRedirectUri, that.devRedirectUri)
      && Objects.equals(backRedirectUri, that.backRedirectUri)
      && Objects.equals(clientId, that.clientId)
      && Objects.equals(clientSecret, that.clientSecret)
      && Arrays.equals(scope, that.scope);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(redirectUri, devRedirectUri, backRedirectUri, clientId, clientSecret);
    result = 31 * result + Arrays.hashCode(scope);
    return result;
  }
}
