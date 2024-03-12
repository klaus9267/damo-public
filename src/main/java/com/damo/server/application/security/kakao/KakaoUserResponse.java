package com.damo.server.application.security.kakao;

import com.damo.server.application.security.provider.OAuthProviderType;
import com.damo.server.domain.user.UserRole;
import com.damo.server.domain.user.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDateTime;

/**
 * Kakao OAuth에서 받은 사용자 정보를 담는 Immutable(불변) 레코드 클래스입니다.
 * {@code JsonNaming} 애너테이션을 통해 JSON 직렬화 시에 속성 이름을 Snake Case로 변환합니다.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoUserResponse(
    Long id,
    boolean hasSignedUp,
    LocalDateTime connectedAt,
    KakaoAccount kakaoAccount
) {
  /**
   * 해당 Kakao 사용자 정보를 도메인 객체로 변환하여 반환하는 메서드입니다.
   *
   * @return Kakao 사용자 정보를 담은 {@link User} 도메인 객체
   */
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

  /**
   * Kakao 사용자의 계정 정보를 담는 Immutable(불변) 레코드 클래스입니다.
   */
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

  /**
   * Kakao 사용자의 프로필 정보를 담는 Immutable(불변) 레코드 클래스입니다.
   */
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record Profile(
      String nickname,
      String thumbnailImageUrl,
      String profileImageUrl,
      boolean isDefaultImage
  ) {
  }
}