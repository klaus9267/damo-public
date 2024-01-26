package com.damo.server.application.config.oauth;

import com.damo.server.application.config.jwt.JwtTokenService;
import com.damo.server.application.config.oauth.provider.OAuthCodeRequestUrlProviderComposite;
import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import com.damo.server.application.config.jwt.JwtToken;
import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import com.damo.server.domain.user.dto.UserWithTokenDto;
import com.damo.server.domain.user.entity.RefreshToken;
import com.damo.server.domain.user.entity.User;
import com.damo.server.domain.user.repository.RefreshTokenRepository;
import com.damo.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthService {
  private final OAuthCodeRequestUrlProviderComposite requestUrlProviderComposite;
  private final OAuthUserClientComposite oauthMemberClientComposite;
  private final UserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  private final PasswordEncoder passwordEncoder;
  private final JwtTokenService jwtTokenService;
  private final AuthenticationManager authenticationManager;

  public String getAuthCodeRequestUrl(final OAuthProviderType providerType, final boolean isDev) {
    return requestUrlProviderComposite.provide(providerType, isDev);
  }

  @Transactional
  public UserWithTokenDto login(final OAuthProviderType providerType, final String authCode, final boolean isDev) {
    final User fetchedUser = oauthMemberClientComposite.fetch(providerType, authCode, isDev);
    final String originProviderId = fetchedUser.getProviderId();
    final User user = userRepository.findOneByUsername(fetchedUser.getUsername())
        .orElseGet(() -> {
          fetchedUser.changeProviderId(passwordEncoder.encode(fetchedUser.getProviderId()));
          return userRepository.save(fetchedUser);
        });
    final String username = user.getUsername();

    final UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(username, originProviderId);
    final Authentication authentication = authenticationManager.authenticate(authenticationToken);

    try {
      final JwtToken jwtToken = jwtTokenService.generateToken(authentication);
      final String refreshToken = jwtToken.getRefreshToken();

      final Optional<RefreshToken> foundRefreshToken =
          refreshTokenRepository.findOneByUsername(username);
      if (foundRefreshToken.isEmpty()) {
        refreshTokenRepository.save(new RefreshToken(refreshToken, username));
      } else {
        foundRefreshToken.get().changeRefreshToken(refreshToken);
      }

      return UserWithTokenDto.from(user, jwtToken.getAccessToken());
    } catch (final Exception e) {
      throw new CustomException(CustomErrorCode.INTERNAL_SERVER_ERROR, "토큰 발급에 실패했습니다.");
    }
  }

  public JwtToken reauthenticateToken(final String token) {
    final String resolvedToken = jwtTokenService.resolveToken(token);
    if (jwtTokenService.validateToken(resolvedToken)) {
      return JwtToken.builder().accessToken(resolvedToken).build();
    }

    try {
      final Authentication authentication = jwtTokenService.getAuthentication(resolvedToken);
      return jwtTokenService.generateToken(authentication);
    } catch (final Exception e) {
      throw new CustomException(CustomErrorCode.INTERNAL_SERVER_ERROR, "토큰 발급에 실패했습니다.");
    }
  }
}
