package com.damo.server.application.config.new_oauth.provider;

public interface OAuthCodeRequestUrlProvider {
    OAuthProviderType providerType();
    String provide();
}
