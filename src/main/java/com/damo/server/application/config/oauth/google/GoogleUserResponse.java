package com.damo.server.application.config.oauth.google;

import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import com.damo.server.domain.user.UserRole;
import com.damo.server.domain.user.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

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