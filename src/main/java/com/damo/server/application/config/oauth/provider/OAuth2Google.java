package com.damo.server.application.config.oauth.provider;

import java.util.Map;

public class OAuth2Google implements OAuth2Provider {
    private final Map<String, Object> attributes; // oauth2User.getAttributes();

    public OAuth2Google(Map<String, Object> attributes) {
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
