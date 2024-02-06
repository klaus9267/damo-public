package com.damo.server.domain.user.dto;

import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import com.damo.server.domain.user.UserRole;
import com.damo.server.domain.user.entity.User;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.ToString;

/**
 * `UserDto` 클래스는 사용자 정보를 전달하는 데이터 전송 객체(DTO)입니다.
 */
@Getter
@ToString
public class UserDto {
  private final Long id;
  private String name;
  private final String email;
  private String username;
  private final UserRole role;
  private OAuthProviderType providerType;
  private String providerId;
  private Timestamp createdAt;

  /**
   * 토큰 생성을 위한 생성자로, ID, 이메일, 사용자 역할을 받아 객체를 초기화합니다.
   *
   * @param id     사용자 ID
   * @param email  사용자 이메일
   * @param userRole 사용자 역할
   */
  public UserDto(final Long id, final String email, final UserRole userRole) {
    this.id = id;
    this.email = email;
    this.role = userRole;
  }

  /**
   * 내부 생성자로, 사용자 엔터티에서 데이터를 이용해 DTO를 생성합니다.
   *
   * @param user 사용자 엔터티
   */
  private UserDto(final User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.email = user.getEmail();
    this.username = user.getUsername();
    this.role = user.getRole();
    this.providerType = user.getProviderType();
    this.providerId = user.getProviderId();
    this.createdAt = user.getCreatedAt();
  }

  /**
   * 사용자 엔터티에서 `UserDto`로 변환합니다.
   *
   * @param user 사용자 엔터티
   * @return `UserDto` 객체
   */
  public static UserDto from(final User user) {
    return new UserDto(user);
  }
}
