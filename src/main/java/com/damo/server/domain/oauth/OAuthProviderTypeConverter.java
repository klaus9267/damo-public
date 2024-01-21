package com.damo.server.domain.oauth;


import org.springframework.core.convert.converter.Converter;

public class OAuthProviderTypeConverter implements Converter<String, OAuthProviderType> {
    @Override
    public OAuthProviderType convert(final String source) {
        return OAuthProviderType.from(source);
    }
}
