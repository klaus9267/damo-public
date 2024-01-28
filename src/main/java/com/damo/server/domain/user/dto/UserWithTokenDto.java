package com.damo.server.domain.user.dto;

import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import com.damo.server.domain.user.entity.User;
import lombok.Getter;

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

  public static UserWithTokenDto from(final User user, final String token) {
    return new UserWithTokenDto(user, token);
  }
}
