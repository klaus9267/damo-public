package com.damo.server.application.config.oauth.naver;

import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import com.damo.server.domain.user.UserRole;
import com.damo.server.domain.user.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * 네이버 OAuth로부터 받은 사용자 정보를 담는 Immutable(불변) 레코드 클래스입니다.
 * {@code JsonNaming} 애너테이션을 통해 JSON 직렬화 시에 속성 이름을 Snake Case로 변환합니다.
 */
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record NaverUserResponse(
    String resultCode,
    String message,
    Response response
) {
  /**
   * {@code NaverUserResponse} 객체를 {@code User} 도메인 객체로 변환하는 메서드입니다.
   *
   * @return {@code User} 도메인 객체
   */
  public User toDomain() {
    return User.builder()
      .name(response.name)
      .email(response.email)
      .role(UserRole.USER)
      .username(response.id + "_" + OAuthProviderType.GOOGLE.getKey())
      .providerId(response.id)
      .providerType(OAuthProviderType.NAVER)
      .profileImageUrl(response.profileImage)
      .build();
  }

  /**
   * {@code NaverUserResponse}의 내부 클래스로, 네이버 사용자 정보를 담는 Immutable(불변) 레코드 클래스입니다.
   * {@code JsonNaming} 애너테이션을 통해 JSON 직렬화 시에 속성 이름을 Snake Case로 변환합니다.
   */
  @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record Response(
      String id,
      String nickname,
      String name,
      String email,
      String gender,
      String age,
      String birthday,
      String profileImage,
      String birthyear,
      String mobile
  ) {
  }
}
