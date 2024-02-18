package com.damo.server.application.security.provider;

import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * {@code OAuthProviderType} 열거형은 지원하는 다양한 OAuth 공급자의 타입을 정의합니다.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@AllArgsConstructor
@Getter
public enum OAuthProviderType {
  GOOGLE("GOOGLE", "구글"),
  NAVER("NAVER", "네이버"),
  KAKAO("KAKAO", "카카오");

  private final String key;
  private final String name;

  /**
   * 주어진 문자열 타입에 해당하는 OAuth 공급자 타입을 반환합니다.
   *
   * @param type OAuth 공급자 타입을 나타내는 문자열
   * @return 주어진 문자열 타입에 해당하는 OAuth 공급자 타입
   * @throws CustomException 잘못된 공급자 타입일 경우 발생하는 예외
   */
  public static OAuthProviderType from(final String type) {
    for (OAuthProviderType providerType: OAuthProviderType.values()) {
      if (providerType.getKey().equals(type.toUpperCase(Locale.ENGLISH))) {
        return providerType;
      }
    }
    throw new CustomException(CustomErrorCode.BAD_REQUEST, "잘못된 provider type");
  }
}
