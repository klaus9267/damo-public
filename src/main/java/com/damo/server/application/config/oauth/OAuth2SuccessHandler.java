package com.damo.server.application.config.oauth;

import com.damo.server.application.config.oauth.jwt.JwtHeader;
import com.damo.server.application.config.oauth.jwt.JwtToken;
import com.damo.server.application.config.oauth.jwt.JwtTokenService;
import com.damo.server.domain.user.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenService jwtTokenService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication authentication
    ) throws IOException {
        final UserDto userDto = ((PrincipalDetails) authentication.getPrincipal()).getUser();
        final JwtToken jwtToken = jwtTokenService.generateToken(userDto);
        log.info("{}", jwtToken);

        writeTokenResponse(response, jwtToken);
    }

    private void writeTokenResponse(final HttpServletResponse response, final JwtToken jwtToken) throws IOException {
        final Cookie cookieAccessToken = new Cookie(JwtHeader.AUTHORIZATION.getKey(), jwtToken.accessToken());
        final Cookie cookieRefreshToken = new Cookie(JwtHeader.AUTHORIZATION_REFRESH.getKey(), jwtToken.refreshToken());

        final int maxAge = 60 * 60 * 24 * 15; // 15일
        cookieAccessToken.setMaxAge(maxAge);
        cookieRefreshToken.setMaxAge(maxAge);

        response.addCookie(cookieAccessToken);
        response.addCookie(cookieRefreshToken);

        response.setContentType(JwtHeader.CONTENT_TYPE.getKey());
        response.sendRedirect("http://localhost:3000"); // TODO: 추후 환경변수로 변경 필요

        final var writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(jwtToken));
        writer.flush();
    }
}

