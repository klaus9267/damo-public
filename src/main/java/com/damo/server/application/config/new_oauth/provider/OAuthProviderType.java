package com.damo.server.application.config.new_oauth.provider;

import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

@AllArgsConstructor
@Getter
public enum OAuthProviderType {
    GOOGLE("GOOGLE", "구글"),
    NAVER("NAVER", "네이버"),
    KAKAO("KAKAO", "카카오");

    private final String key;
    private final String name;

    public static OAuthProviderType from(final String type) {
        for(OAuthProviderType providerType: OAuthProviderType.values()) {
            if(providerType.getKey().equals(type.toUpperCase(Locale.ENGLISH))) {
                return providerType;
            }
        }
        throw new CustomException(CustomErrorCode.BAD_REQUEST, "잘못된 provider type");
    }
}
