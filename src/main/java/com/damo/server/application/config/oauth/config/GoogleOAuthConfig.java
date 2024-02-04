package com.damo.server.application.config.oauth.config;

import java.util.Arrays;
import java.util.Objects;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Google OAuth 설정을 나타내는 불변(immutable) 레코드 클래스입니다.
 * {@code ConfigurationProperties} 애너테이션을 통해 프로퍼티 값들을 읽어오며, 프로퍼티의 prefix는 "oauth.google" 입니다.
 * 대상 필드들은 {@code toString}, {@code equals}, {@code hashCode} 메서드에서 고려됩니다.
 *
 * @param redirectUri      OAuth 리다이렉트 URI
 * @param devRedirectUri   개발 환경에서 사용되는 OAuth 리다이렉트 URI
 * @param backRedirectUri  백엔드 서버에서 사용되는 OAuth 리다이렉트 URI
 * @param clientId         OAuth 클라이언트 ID
 * @param clientSecret     OAuth 클라이언트 시크릿
 * @param scope            OAuth 스코프를 담고 있는 배열
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@ConfigurationProperties(prefix = "oauth.google")
public record GoogleOAuthConfig(
    String redirectUri,
    String devRedirectUri,
    String backRedirectUri,
    String clientId,
    String clientSecret,
    String[] scope
) {
  @Override
  public String toString() {
    return "GoogleOAuthConfig{"
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
    final GoogleOAuthConfig that = (GoogleOAuthConfig) o;
    return Objects.equals(redirectUri, that.redirectUri)
      && Objects.equals(devRedirectUri, that.devRedirectUri)
      && Objects.equals(backRedirectUri, that.backRedirectUri)
      && Objects.equals(clientId, that.clientId)
      && Objects.equals(clientSecret, that.clientSecret)
      && Arrays.equals(scope, that.scope);
  }

  /**
   * 객체의 해시 코드를 생성하는 메서드입니다. 객체의 각 필드에 대한 해시 코드를 조합하여 반환하며,
   * 배열 필드인 scope는 Arrays.hashCode를 통해 내용을 기반으로 한 해시 코드를 추가합니다.
   */
  public int hashCode() {
    int result = Objects.hash(
        redirectUri,
        devRedirectUri,
        backRedirectUri,
        clientId,
        clientSecret
    );
    result = 31 * result + Arrays.hashCode(scope);
    return result;
  }
}
