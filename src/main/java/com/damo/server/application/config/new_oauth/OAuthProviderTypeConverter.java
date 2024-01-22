package com.damo.server.application.config.new_oauth;


import com.damo.server.application.config.new_oauth.provider.OAuthProviderType;
import org.springframework.core.convert.converter.Converter;

public class OAuthProviderTypeConverter implements Converter<String, OAuthProviderType> {
    @Override
    public OAuthProviderType convert(final String source) {
        return OAuthProviderType.from(source);
    }
}
