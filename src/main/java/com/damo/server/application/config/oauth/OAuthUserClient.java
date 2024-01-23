package com.damo.server.application.config.oauth;

import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import com.damo.server.domain.user.entity.User;

public interface OAuthUserClient {
    OAuthProviderType providerType();
    User fetch(final String code, final boolean isDev);
}
