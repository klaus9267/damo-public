package com.damo.server.application.config.oauth.provider;

public interface OAuthCodeRequestUrlProvider {
    OAuthProviderType providerType();
    String provide();
}
