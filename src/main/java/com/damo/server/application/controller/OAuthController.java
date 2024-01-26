package com.damo.server.application.controller;

import com.damo.server.application.config.jwt.JwtToken;
import com.damo.server.application.config.oauth.OAuthService;
import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * OAuthController 는 대문자 연속을 허용하는 게 더 깔끔해 보여서 SuppressWarnings 어노테이션 적용.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
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
  @SneakyThrows
  @GetMapping("{provider}")
  public ResponseEntity<Void> redirectAuthCodeRequestUrl(
      @PathVariable("provider") final OAuthProviderType providerType,
      final HttpServletRequest request,
      final HttpServletResponse response
  ) {
    final boolean isDev = checkLocalhostFromRequest(request);
    final String redirectUrl = authService.getAuthCodeRequestUrl(providerType, isDev);

    response.sendRedirect(redirectUrl);

    return ResponseEntity.ok().build();
  }

  /**
   * 로그인 처리를 위한 메서드입니다.
   * 특정 제공자 유형과 받은 인증 코드를 기반으로 사용자를 인증하고, JwtToken 을 생성하여 반환합니다.
   */
  @GetMapping("/login/{provider}")
  public ResponseEntity<JwtToken> login(
      @PathVariable("provider") final OAuthProviderType providerType,
      @RequestParam("code") final String code,
      final HttpServletRequest request
  ) {
    final boolean isDev = checkLocalhostFromRequest(request);
    final JwtToken jwtToken = authService.login(providerType, code, isDev);

    return ResponseEntity.ok(jwtToken);
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
}
