package com.damo.server.application.controller;

import com.damo.server.application.config.oauth.jwt.JwtHeader;
import com.damo.server.application.config.oauth.jwt.JwtToken;
import com.damo.server.application.config.oauth.jwt.JwtTokenService;
import com.damo.server.application.handler.exception.BadRequestException;
import com.damo.server.application.handler.exception.UnauthorizedException;
import com.damo.server.domain.user.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("oauth")
public class OAuthController {
    private final JwtTokenService jwtTokenService;

    // OAuth 연동 확인을 위한 임시 API
    @GetMapping
    public Map<String, Object> tempOAuth2(@AuthenticationPrincipal OAuth2User oauth) {
        return oauth.getAttributes();
    }

    @GetMapping("token/expired")
    public void checkExpiredToken() {
        throw new UnauthorizedException("토큰 만료");
    }

    @GetMapping("token/refresh")
    public void refreshToken(final HttpServletRequest request, final HttpServletResponse response) {
        final String token = request.getHeader(JwtHeader.AUTHORIZATION_REFRESH.getKey());

        if (StringUtils.isEmpty(token)) {
            throw new BadRequestException("잘못된 토큰 데이터");
        }
        if (!jwtTokenService.verifyToken(token)) {
            throw new UnauthorizedException("만료 혹은 위조된 토큰 데이터");
        }

        final UserDto user = jwtTokenService.getUserFromToken(token);
        final JwtToken newJwtToken = jwtTokenService.generateToken(user);

        response.addHeader(JwtHeader.AUTHORIZATION.getKey(), newJwtToken.accessToken());
        response.addHeader(JwtHeader.AUTHORIZATION_REFRESH.getKey(), newJwtToken.refreshToken());
        response.setContentType(JwtHeader.CONTENT_TYPE.getKey());
    }
}
