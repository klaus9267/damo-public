package com.damo.server.application.config.oauth.strategy;

import com.damo.server.application.config.oauth.provider.OAuth2Kakao;
import com.damo.server.application.config.oauth.provider.OAuth2Provider;

import java.util.Map;

public class KakaoProviderStrategy implements OAuth2ProviderStrategy {
    @Override
    public OAuth2Provider getProvider(Map<String, Object> attributes) {
        return new OAuth2Kakao(attributes);
    }
}