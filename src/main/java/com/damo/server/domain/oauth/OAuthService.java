package com.damo.server.domain.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final OAuthCodeRequestUrlProviderComposite oAuthCodeRequestUrlProviderComposite;
    private final OAuthMemberClientComposite oauthMemberClientComposite;
    private final OAuthMemberRepository oauthMemberRepository;

    public String getAuthCodeRequestUrl(final OAuthProviderType oAuthProviderType) {
        return oAuthCodeRequestUrlProviderComposite.provide(oAuthProviderType);
    }

    public Long login(final OAuthProviderType oAuthProviderType, final String authCode) {
        final OAuthMember oAuthMember = oauthMemberClientComposite.fetch(oAuthProviderType, authCode);
        final OAuthMember saved = oauthMemberRepository.findByOAuthId(oAuthMember.getOAuthId().getOAuthProviderId(), oAuthMember.getOAuthId().getOAuthProviderType())
                .orElseGet(() -> oauthMemberRepository.save(oAuthMember));

        return saved.getId();
    }
}
