package com.damo.server.application.config.oauth;

import com.damo.server.application.config.oauth.provider.OAuth2Provider;
import com.damo.server.application.config.oauth.provider.ProviderType;
import com.damo.server.application.config.oauth.strategy.GoogleProviderStrategy;
import com.damo.server.application.config.oauth.strategy.KakaoProviderStrategy;
import com.damo.server.application.config.oauth.strategy.NaverProviderStrategy;
import com.damo.server.application.config.oauth.strategy.OAuth2ProviderStrategy;
import com.damo.server.application.handler.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;

public class OAuth2ProviderFactory {
    private final Map<String, OAuth2ProviderStrategy> strategies = new HashMap<>();;

    public OAuth2ProviderFactory() {
        strategies.put(ProviderType.GOOGLE.getKey().toLowerCase(), new GoogleProviderStrategy());
        strategies.put(ProviderType.NAVER.getKey().toLowerCase(), new NaverProviderStrategy());
        strategies.put(ProviderType.KAKAO.getKey().toLowerCase(), new KakaoProviderStrategy());
    }

    public OAuth2Provider getOAuth2Provider(String registrationId, Map<String, Object> attributes) {
        OAuth2ProviderStrategy strategy = strategies.get(registrationId);
        if (strategy == null) {
            throw new NotFoundException("프로바이더를 찾을 수 없습니다");
        }
        return strategy.getProvider(attributes);
    }
}