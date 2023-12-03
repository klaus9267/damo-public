package com.damo.server.application.config.oauth.provider;

import com.damo.server.domain.user.ProviderType;

public interface OAuth2UserInfo {
    String getProviderId();
    ProviderType getProvider();
    String getEmail();
    String getName();
}
