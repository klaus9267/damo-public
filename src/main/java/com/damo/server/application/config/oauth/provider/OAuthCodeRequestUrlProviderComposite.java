package com.damo.server.application.config.oauth.provider;

import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OAuthCodeRequestUrlProviderComposite {
    private final Map<OAuthProviderType, OAuthCodeRequestUrlProvider> map;

    public OAuthCodeRequestUrlProviderComposite(final Set<OAuthCodeRequestUrlProvider> providers) {
        this.map = providers.stream().collect(Collectors.toMap(OAuthCodeRequestUrlProvider::providerType, Function.identity()));
    }

    public String provide(final OAuthProviderType oAuthProviderType) {
        return getProvider(oAuthProviderType).provide();
    }

    private OAuthCodeRequestUrlProvider getProvider(final OAuthProviderType oAuthProviderType) {
        return Optional.ofNullable(map.get(oAuthProviderType))
                .orElseThrow(() -> new CustomException(CustomErrorCode.UNAUTHORIZED, "지원하지 않는 소셜 로그인"));
    }
}
