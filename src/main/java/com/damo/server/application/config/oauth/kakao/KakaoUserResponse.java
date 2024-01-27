package com.damo.server.application.config.oauth.kakao;

import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import com.damo.server.domain.user.UserRole;
import com.damo.server.domain.user.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoUserResponse(
    Long id,
    boolean hasSignedUp,
    LocalDateTime connectedAt,
    KakaoAccount kakaoAccount
) {

  public User toDomain() {
    return User.builder()
      .name(kakaoAccount.profile.nickname) // TODO: 이름은 스코프에 없음
      .email("") // TODO: 카카오는 배포하기 전까지 이메일을 못가져옴
      .role(UserRole.USER)
      .username(id + "_" + OAuthProviderType.KAKAO.getKey())
      .providerId(String.valueOf(id))
      .providerType(OAuthProviderType.KAKAO)
      .profileImageUrl(kakaoAccount.profile.profileImageUrl)
      .build();
  }

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record KakaoAccount(
      boolean profileNeedsAgreement,
      boolean profileNicknameNeedsAgreement,
      boolean profileImageNeedsAgreement,
      Profile profile,
      boolean nameNeedsAgreement,
      String name,
      boolean emailNeedsAgreement,
      boolean isEmailValid,
      boolean isEmailVerified,
      String email,
      boolean ageRangeNeedsAgreement,
      String ageRange,
      boolean birthyearNeedsAgreement,
      String birthyear,
      boolean birthdayNeedsAgreement,
      String birthday,
      String birthdayType,
      boolean genderNeedsAgreement,
      String gender,
      boolean phoneNumberNeedsAgreement,
      String phoneNumber,
      boolean ciNeedsAgreement,
      String ci,
      LocalDateTime ciAuthenticatedAt
  ) {
  }

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record Profile(
      String nickname,
      String thumbnailImageUrl,
      String profileImageUrl,
      boolean isDefaultImage
  ) {
  }
}