package com.damo.server.application.config.jwt;

import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import com.damo.server.application.handler.exception.ResponseCustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Spring Security AuthenticationEntryPoint 구현하며, 주로 JWT 기반의 인증에서 인증 오류 시 처리를 담당합니다.
 * 주어진 JWT 토큰을 검증하고, 유효한 경우 해당 토큰으로부터 사용자 인증 정보를 얻어 SecurityContextHolder 저장합니다.
 * 유효하지 않은 토큰이나 인증 오류가 발생한 경우, 클라이언트에게 JSON 형식의 오류 응답을 반환합니다.
 */
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
  private final JwtTokenService jwtTokenService;
  private final ObjectMapper objectMapper;

  @Override
  public void commence(
          final HttpServletRequest request,
          final HttpServletResponse response,
          final AuthenticationException authException
  ) throws IOException {
    final String authorization = request.getHeader(JwtToken.HEADER_KEY);
    final String token = jwtTokenService.resolveToken(authorization);

    if (token != null && jwtTokenService.validateToken(token)) {
      final Authentication authentication = jwtTokenService.getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } else {
      final CustomException customException =
          new CustomException(CustomErrorCode.UNAUTHORIZED, "잘못된 토큰입니다.");
      final String body =
          objectMapper.writeValueAsString(ResponseCustomException.of(customException));

      response.setContentType(JwtToken.CONTENT_TYPE);
      response.setStatus(customException.getCustomErrorCode().getStatusCode());
      response.getWriter().write(body);
    }
  }
}
