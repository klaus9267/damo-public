package com.damo.server.domain.user.dto;

import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import com.damo.server.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public class UserWithTokenDto {
  @JsonIgnore
  private final String accessToken;
  private final Long id;
  private final String profileUrl;
  private final String name;
  private final String email;
  private final OAuthProviderType providerType;

  private UserWithTokenDto(final User user, final String token) {
    this.id = user.getId();
    this.profileUrl = user.getProfileUrl();
    this.name = user.getName();
    this.email = user.getEmail();
    this.providerType = user.getProviderType();
    this.accessToken = token;
  }

  public static UserWithTokenDto from(final User user, final String token) {
    return new UserWithTokenDto(user, token);
  }
}
