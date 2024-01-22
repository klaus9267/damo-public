package com.damo.server.application.config.oauth;

import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import com.damo.server.domain.oauth.OAuthMember;

public interface OAuthUserClient {
    OAuthProviderType providerType();
    OAuthMember fetch(final String code);
}
