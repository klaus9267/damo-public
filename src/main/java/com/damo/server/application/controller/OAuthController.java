package com.damo.server.application.controller;

import com.damo.server.application.controller.operation.oauth.OAuthProviderOperation;
import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import com.damo.server.application.security.OAuthService;
import com.damo.server.application.security.jwt.JwtToken;
import com.damo.server.application.security.provider.OAuthProviderType;
import com.damo.server.domain.user.dto.UserWithTokenDto;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * OAuthController 는 대문자 연속을 허용하는 게 더 깔끔해 보여서 SuppressWarnings 어노테이션 적용.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@Tag(name = "OAUTH")
@AllArgsConstructor
@RestController
@RequestMapping("oauth")
public class OAuthController {
  private final OAuthService authService;

  /**
   * OAuth 인증 코드 요청의 리디렉션을 처리합니다.
   * 특정 제공자 유형에 기반하여 인증 코드 요청 URL 생성하고 사용자를 인가 서버로 리디렉션합니다.
   * SneakyThrows 어노테이션을 사용하여 체크된 예외를 처리합니다.
   */
  @OAuthProviderOperation(summary = "OAuth 로그인 페이지 리다이렉트", description = "provider에 맞는 로그인 페이지로 리다이렉트 시켜줍니다.")
  @SneakyThrows
  @GetMapping("{provider}")
  public ResponseEntity<Void> redirectAuthCodeRequestUrl(
      @PathVariable("provider") final @Valid OAuthProviderType providerType,
      final HttpServletRequest request,
      final HttpServletResponse response
  ) {
    final boolean isDev = checkLocalhostFromRequest(request);
    final String redirectUrl = authService.getAuthCodeRequestUrl(providerType, isDev);

    response.sendRedirect(redirectUrl);

    return ResponseEntity.status(HttpStatus.FOUND).build();
  }

  /**
   * 로그인 처리를 위한 메서드입니다.
   * 특정 제공자 유형과 받은 인증 코드를 기반으로 사용자를 인증하고, JwtToken 을 생성하여 반환합니다.
   */
  @GetMapping("/login/{provider}")
  public ResponseEntity<UserWithTokenDto> login(
      @PathVariable("provider") final OAuthProviderType providerType,
      @RequestParam("code") final String code,
      final HttpServletRequest request
  ) {
    final boolean isDev = checkLocalhostFromRequest(request);
    final UserWithTokenDto userWithTokenDto = authService.login(providerType, code, isDev);

    return ResponseEntity.ok(userWithTokenDto);
  }

  private boolean checkLocalhostFromRequest(final HttpServletRequest request) {
    final String referer = request.getHeader("Referer");
    return referer != null
      && (referer.contains("localhost") || referer.contains("127.0.0.1"));
  }

  /**
   * 백엔드 전용 oauth redirect api.
   */
  @Hidden
  @GetMapping("/back/{provider}")
  public void redirectAuthCodeForBack(
      @PathVariable("provider") final OAuthProviderType providerType,
      @RequestParam("code") final String code,
      final HttpServletResponse response
  ) throws IOException {
    final String redirectUri = UriComponentsBuilder
        .fromUriString("http://localhost:8080/oauth/login/" + providerType.getKey())
        .queryParam("code", code)
        .toUriString();
    response.sendRedirect(redirectUri);
  }

  /**
   * 만료된 JWT 토큰을 다시 인증하고 새 토큰을 발급하여 반환하는 API.
   * 요청에서 토큰을 추출하고, 토큰이 없으면 예외를 발생시킴.
   */
  @GetMapping("/token/expired")
  public ResponseEntity<UserWithTokenDto> reauthenticateToken(final HttpServletRequest request) {
    final String token = request.getHeader(JwtToken.HEADER_KEY);
    if (token == null) {
      throw new CustomException(CustomErrorCode.UNAUTHORIZED, "토큰을 확인해 주세요.");
    }

    final UserWithTokenDto userWithTokenDto = authService.reauthenticateToken(token);

    return ResponseEntity.ok(userWithTokenDto);
  }

  /**
   * HTTP 요청에서 JWT 토큰을 추출하고 해당 토큰이 유효한지 검증하는 메서드입니다.
   */
  @GetMapping("/token/validated")
  public ResponseEntity<Boolean> validateToken(final HttpServletRequest request) {
    final String token = request.getHeader(JwtToken.HEADER_KEY);

    authService.validateToken(token);

    return ResponseEntity.noContent().build();
  }
}
