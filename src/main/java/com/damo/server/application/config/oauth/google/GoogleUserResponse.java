package com.damo.server.application.config.oauth.google;

import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import com.damo.server.domain.user.UserRole;
import com.damo.server.domain.user.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Google OAuth에서 받은 사용자 정보를 담는 Immutable(불변) 레코드 클래스입니다.
 * {@code JsonNaming} 애너테이션을 통해 JSON 직렬화 시에 속성 이름을 Snake Case로 변환합니다.
 */
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoogleUserResponse(
    String id,
    String email,
    Boolean verifiedEmail,
    String name,
    String givenName,
    String familyName,
    String picture,
    String locale
) {
  /**
   * Google 사용자 정보를 도메인 객체로 변환하여 반환하는 메서드입니다.
   */
  public User toDomain() {
    return User.builder()
      .name(name)
      .email(email)
      .role(UserRole.USER)
      .username(id + "_" + OAuthProviderType.GOOGLE.getKey())
      .providerId(id)
      .providerType(OAuthProviderType.GOOGLE)
      .profileImageUrl(picture)
      .build();
  }
}