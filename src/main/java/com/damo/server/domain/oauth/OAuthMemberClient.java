package com.damo.server.domain.oauth;

public interface OAuthMemberClient {
    OAuthProviderType providerType();
    OAuthMember fetch(final String code);
}
