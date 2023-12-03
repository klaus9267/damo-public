package com.damo.server.application.config.oauth.provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProviderType {

    GOOGLE("GOOGLE", "구글"),
    NAVER("NAVER", "네이버"),
    KAKAO("KAKAO", "카카오");

    private final String key;
    private final String title;
}
