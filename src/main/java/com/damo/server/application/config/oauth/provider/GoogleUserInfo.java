package com.damo.server.application.config.oauth.provider;

import com.damo.server.domain.user.ProviderType;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo {
    private final Map<String, Object> attributes; // oauth2User.getAttributes();

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get("sub").toString();
    }

    @Override
    public ProviderType getProvider() {
        return ProviderType.GOOGLE;
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }
}
