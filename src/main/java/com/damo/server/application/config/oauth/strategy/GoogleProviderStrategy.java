package com.damo.server.application.config.oauth.strategy;

import com.damo.server.application.config.oauth.provider.OAuth2Google;
import com.damo.server.application.config.oauth.provider.OAuth2Provider;

import java.util.Map;

public class GoogleProviderStrategy implements OAuth2ProviderStrategy {
    @Override
    public OAuth2Provider getProvider(Map<String, Object> attributes) {
        return new OAuth2Google(attributes);
    }
}