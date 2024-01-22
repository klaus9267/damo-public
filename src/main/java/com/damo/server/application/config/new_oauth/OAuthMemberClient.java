package com.damo.server.application.config.new_oauth;

import com.damo.server.application.config.new_oauth.provider.OAuthProviderType;
import com.damo.server.domain.oauth.OAuthMember;

public interface OAuthMemberClient {
    OAuthProviderType providerType();
    OAuthMember fetch(final String code);
}
