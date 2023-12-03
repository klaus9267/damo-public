package com.damo.server.application.config.oauth.provider;

import java.util.Map;

public class OAuth2Kakao implements OAuth2Provider {
    private Map<String, Object> attributes; // oauth2User.getAttributes();
    private Map<String, Object> properties;

    public OAuth2Kakao(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.properties = (Map<String, Object>) attributes.get("properties");
    }

    // TODO: 카카오 로그인 정보 승인받으면 리턴값 수정
    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public ProviderType getProvider() {
        return ProviderType.KAKAO;
    }

    @Override
    public String getEmail() { return "EMAIL"; }

    @Override
    public String getName() {
        return properties.get("nickname").toString();
    }

    @Override
    public String getUsername() {
        return getProvider() + "_" + getProviderId();
    }
}