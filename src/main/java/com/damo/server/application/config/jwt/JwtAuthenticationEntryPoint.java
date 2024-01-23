package com.damo.server.application.config.jwt;

import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import com.damo.server.application.handler.exception.ResponseCustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

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
        final String authorization = request.getHeader("Authorization");
        final String token = jwtTokenService.resolveToken(authorization);

        if(token != null && jwtTokenService.validateToken(token)) {
            final Authentication authentication = jwtTokenService.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            final CustomException customException = new CustomException(CustomErrorCode.UNAUTHORIZED, "유저 인증 정보가 잘못됐습니다.");
            final String body = objectMapper.writeValueAsString(ResponseCustomException.of(customException));

            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(customException.getCustomErrorCode().getStatusCode());
            response.getWriter().write(body);
        }
    }
}
