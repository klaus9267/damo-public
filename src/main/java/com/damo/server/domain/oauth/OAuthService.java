package com.damo.server.domain.oauth;

import com.damo.server.application.config.jwt.JwtTokenService;
import com.damo.server.application.config.oauth.OAuthMemberClientComposite;
import com.damo.server.application.config.oauth.provider.OAuthCodeRequestUrlProviderComposite;
import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import com.damo.server.application.config.jwt.JwtToken;
import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final OAuthCodeRequestUrlProviderComposite oAuthCodeRequestUrlProviderComposite;
    private final OAuthMemberClientComposite oauthMemberClientComposite;
    private final OAuthMemberRepository oauthMemberRepository;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenService jwtTokenService;

    public String getAuthCodeRequestUrl(final OAuthProviderType oAuthProviderType) {
        return oAuthCodeRequestUrlProviderComposite.provide(oAuthProviderType);
    }

    public JwtToken login(final OAuthProviderType oAuthProviderType, final String authCode) {
        final OAuthMember oAuthMember = oauthMemberClientComposite.fetch(oAuthProviderType, authCode);
        final OAuthMember saved = oauthMemberRepository.findByOAuthId(oAuthMember.getOAuthId().getOAuthProviderId(), oAuthMember.getOAuthId().getOAuthProviderType())
                .orElseGet(() -> oauthMemberRepository.save(oAuthMember));


        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(saved.getId(), saved.getOAuthId().getOAuthProviderId());
        final Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        try {
            return jwtTokenService.generateToken(authentication);
        } catch (final Exception e) {
            throw new CustomException(CustomErrorCode.INTERNAL_SERVER_ERROR, "토큰 발급에 실패했습니다.");
        }
    }
}
