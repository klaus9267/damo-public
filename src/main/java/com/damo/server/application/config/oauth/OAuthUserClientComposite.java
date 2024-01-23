package com.damo.server.application.config.oauth;

import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import com.damo.server.domain.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OAuthUserClientComposite {
    private final Map<OAuthProviderType, OAuthUserClient> map;

    public OAuthUserClientComposite(final Set<OAuthUserClient> clients) {
        this.map = clients.stream().collect(Collectors.toMap(OAuthUserClient::providerType, Function.identity()));
    }

    public User fetch(final OAuthProviderType oAuthProviderType, final String authCode, final boolean isDev) {
        return getClient(oAuthProviderType).fetch(authCode, isDev);
    }

    private OAuthUserClient getClient(OAuthProviderType oAuthProviderType) {
        return Optional.ofNullable(map.get(oAuthProviderType))
                .orElseThrow(() -> new CustomException(CustomErrorCode.UNAUTHORIZED, "지원하지 않는 소셜 로그인"));
    }
}
