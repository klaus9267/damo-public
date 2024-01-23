package com.damo.server.application.config.oauth;

import com.damo.server.application.config.jwt.JwtTokenService;
import com.damo.server.application.config.oauth.provider.OAuthCodeRequestUrlProviderComposite;
import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import com.damo.server.application.config.jwt.JwtToken;
import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import com.damo.server.domain.user.entity.User;
import com.damo.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final OAuthCodeRequestUrlProviderComposite oAuthCodeRequestUrlProviderComposite;
    private final OAuthUserClientComposite oauthMemberClientComposite;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;

    public String getAuthCodeRequestUrl(final OAuthProviderType oAuthProviderType, final boolean isDev) {
        return oAuthCodeRequestUrlProviderComposite.provide(oAuthProviderType, isDev);
    }

    public JwtToken login(final OAuthProviderType oAuthProviderType, final String authCode) {
        final User user = oauthMemberClientComposite.fetch(oAuthProviderType, authCode);
        final String originProviderId = user.getProviderId();
        final User saved = userRepository.findOneByUsername(user.getUsername())
                .orElseGet(() -> {
                    user.changeProviderId(passwordEncoder.encode(user.getProviderId()));
                    return userRepository.save(user);
                });

        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(saved.getUsername(), originProviderId);
        final Authentication authentication = authenticationManager.authenticate(authenticationToken);

        try {
            return jwtTokenService.generateToken(authentication);
        } catch (final Exception e) {
            throw new CustomException(CustomErrorCode.INTERNAL_SERVER_ERROR, "토큰 발급에 실패했습니다.");
        }
    }
}
