package com.damo.server.application.config.oauth.strategy;

import com.damo.server.application.config.oauth.provider.OAuth2Provider;

import java.util.Map;

public interface OAuth2ProviderStrategy {
    OAuth2Provider getProvider(Map<String, Object> attributes);
}
