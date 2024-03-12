package com.damo.server.application.security;

import com.damo.server.application.security.provider.OAuthProviderType;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

/**
 * {@code OAuthProviderTypeConverter} 클래스는 문자열을 {@code OAuthProviderType}으로 변환하는 역할을 합니다.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class OAuthProviderTypeConverter implements Converter<String, OAuthProviderType> {
  @Override
  public OAuthProviderType convert(@NonNull final String source) {
    return OAuthProviderType.from(source);
  }
}
