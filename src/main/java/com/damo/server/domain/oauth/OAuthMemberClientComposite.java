package com.damo.server.domain.oauth;

import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OAuthMemberClientComposite {
    private final Map<OAuthProviderType, OAuthMemberClient> map;

    public OAuthMemberClientComposite(final Set<OAuthMemberClient> clients) {
        this.map = clients.stream()
                .collect(Collectors.toMap(OAuthMemberClient::providerType, Function.identity()));
    }

    public OAuthMember fetch(final OAuthProviderType oAuthProviderType, final String authCode) {
        return getClient(oAuthProviderType).fetch(authCode);
    }

    private OAuthMemberClient getClient(OAuthProviderType oAuthProviderType) {
        return Optional.ofNullable(map.get(oAuthProviderType))
                .orElseThrow(() -> new CustomException(CustomErrorCode.UNAUTHORIZED, "지원하지 않는 소셜 로그인"));
    }
}
