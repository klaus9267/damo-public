package com.damo.server.domain.user.dto;

import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import com.damo.server.domain.user.entity.User;
import lombok.Getter;

/**
 * `UserWithTokenDto` 클래스는 토큰과 함께 사용자 정보를 전달하는 데이터 전송 객체(DTO)입니다.
 */
@Getter
public class UserWithTokenDto {
  private final String accessToken;
  private final Long id;
  private final String profileImageUrl;
  private final String name;
  private final String email;
  private final OAuthProviderType providerType;

  private UserWithTokenDto(final User user, final String token) {
    this.id = user.getId();
    this.profileImageUrl = user.getProfileImageUrl();
    this.name = user.getName();
    this.email = user.getEmail();
    this.providerType = user.getProviderType();
    this.accessToken = token;
  }

  /**
   * 정적 팩토리 메서드로, 사용자 엔터티와 토큰을 이용해 `UserWithTokenDto` 객체를 생성합니다.
   *
   * @param user  사용자 엔터티
   * @param token 토큰 값
   * @return `UserWithTokenDto` 객체
   */
  public static UserWithTokenDto from(final User user, final String token) {
    return new UserWithTokenDto(user, token);
  }
}
