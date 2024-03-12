package com.damo.server.application.security;

import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import com.damo.server.application.security.jwt.JwtToken;
import com.damo.server.application.security.jwt.JwtTokenService;
import com.damo.server.application.security.provider.OAuthCodeRequestUrlProviderComposite;
import com.damo.server.application.security.provider.OAuthProviderType;
import com.damo.server.application.security.user_details.CustomUserDetails;
import com.damo.server.domain.common.exception.ExceptionThrowHelper;
import com.damo.server.domain.user.dto.UserWithTokenDto;
import com.damo.server.domain.user.entity.RefreshToken;
import com.damo.server.domain.user.entity.User;
import com.damo.server.domain.user.repository.RefreshTokenRepository;
import com.damo.server.domain.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


/**
 * 외부 OAuth 서비스로부터 인증 코드를 받아와 로그인을 수행하고, 새로운 액세스 토큰 및 리프레시 토큰을 반환합니다.
 * 사용자가 이미 존재할 경우 새로운 로그인 기록 생성 없이 기존 사용자 정보를 사용하며,
 * 새로운 사용자일 경우 사용자 정보를 저장하고 패스워드 인코딩을 적용합니다.
 * 토큰 발급에 성공하면 새로운 리프레시 토큰이 저장되거나 갱신됩니다.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
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

  /**
   * 외부 OAuth 서비스로부터 받은 인증 코드를 사용하여 로그인을 수행하고, 새로운 액세스 토큰 및 리프레시 토큰을 반환합니다.
   * 사용자가 이미 존재하는 경우, 새로운 로그인 기록이 생성되지 않고 기존 사용자 정보가 사용됩니다.
   * 새로운 사용자인 경우, 사용자 정보가 저장되고 패스워드 인코딩이 적용됩니다.
   * 토큰 발급에 성공하면, 새로운 리프레시 토큰이 저장되거나 갱신됩니다.
   */
  @Transactional
  public UserWithTokenDto login(
      final OAuthProviderType providerType,
      final String authCode,
      final boolean isDev
  ) {
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

  /**
   * 주어진 JWT 토큰을 재인증하고 새로운 액세스 토큰 및 리프레시 토큰을 반환하는 메서드.
   * 토큰 해석, 사용자 정보 확인 및 유효성 검사 후, 새로운 토큰 발급.
   * 만료된 경우 리프레시 토큰을 사용하여 갱신.
   * 만약 실패하면 예외 발생.
   */
  public UserWithTokenDto reauthenticateToken(final String token) {
    final String resolvedToken = jwtTokenService.resolveToken(token);
    final Authentication authentication = jwtTokenService.getAuthentication(resolvedToken);
    final CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
    final String username = customUserDetails.getUsername();

    final User user = userRepository
        .findOneByUsername(username)
        .orElseThrow(ExceptionThrowHelper.throwNotFound("유저 정보를 찾을 수 없습니다."));

    if (jwtTokenService.validateToken(resolvedToken)) {
      return UserWithTokenDto.from(user, resolvedToken);
    }


    final RefreshToken refreshToken = refreshTokenRepository
        .findOneByUsername(username)
        .orElseThrow(ExceptionThrowHelper.throwNotFound("Refresh Token이 존재하지 않습니다."));
    if (!jwtTokenService.validateToken(refreshToken.getToken())) {
      throw new CustomException(CustomErrorCode.UNAUTHORIZED, "리프레쉬 토큰이 만료되었습니다.");
    }

    try {
      final JwtToken jwtToken = jwtTokenService.generateToken(authentication);
      final String newRefreshToken = jwtToken.getRefreshToken();

      refreshToken.changeRefreshToken(newRefreshToken);

      return UserWithTokenDto.from(user, jwtToken.getAccessToken());
    } catch (final Exception e) {
      throw new CustomException(CustomErrorCode.INTERNAL_SERVER_ERROR, "토큰 발급에 실패했습니다.");
    }
  }

  /**
   * 주어진 토큰이 유효한지 검증하는 메서드입니다.
   * 토큰이 비어있지 않고 유효한 경우 true를 반환하며, 그렇지 않으면 false를 반환합니다.
   */
  public void validateToken(final String token) {
    final boolean isValid = StringUtils.hasText(token)
        && jwtTokenService.validateToken(jwtTokenService.resolveToken(token));

    if (!isValid) {
      throw new CustomException(CustomErrorCode.UNAUTHORIZED, "잘못된 토큰입니다.");
    }
  }
}
