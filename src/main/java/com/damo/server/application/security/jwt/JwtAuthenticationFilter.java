package com.damo.server.application.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * request 요청에서 Authorization 헤더를 추출해 토큰을 검증하는 로직.
 */
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtTokenService jwtTokenService;

  @Override
  protected void doFilterInternal(
      @NonNull final HttpServletRequest request,
      @NonNull final HttpServletResponse response,
      @NonNull final FilterChain filterChain
  ) throws ServletException, IOException {
    final String authorization = request.getHeader(JwtToken.HEADER_KEY);
    final String token = jwtTokenService.resolveToken(authorization);

    if (token != null && jwtTokenService.validateToken(token)) {
      final Authentication authentication = jwtTokenService.getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }
}