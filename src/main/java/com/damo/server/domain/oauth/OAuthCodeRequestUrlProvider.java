package com.damo.server.domain.oauth;

public interface OAuthCodeRequestUrlProvider {
    OAuthProviderType providerType();
    String provide();
}
