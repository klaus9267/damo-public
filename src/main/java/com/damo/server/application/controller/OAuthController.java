package com.damo.server.application.controller;

import com.damo.server.application.config.oauth.jwt.JwtHeader;
import com.damo.server.application.config.oauth.jwt.JwtToken;
import com.damo.server.application.config.oauth.jwt.JwtTokenService;
import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import com.damo.server.domain.oauth.OAuthProviderType;
import com.damo.server.domain.oauth.OAuthService;
import com.damo.server.domain.user.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("oauth")
public class OAuthController {
    private final JwtTokenService jwtTokenService;
    private final OAuthService oAuthService;

    @SneakyThrows
    @GetMapping("{oAuthProviderType}")
    public ResponseEntity<Void> redirectAuthCodeRequestUrl(
            @PathVariable("oAuthProviderType") final OAuthProviderType oAuthProviderType,
            final HttpServletResponse response
    ) {
        final String redirectUrl = oAuthService.getAuthCodeRequestUrl(oAuthProviderType);

        response.sendRedirect(redirectUrl);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/{oAuthProviderType}")
    public ResponseEntity<Long> login(
            @PathVariable("oAuthProviderType") final OAuthProviderType oAuthProviderType,
            @RequestParam("code") final String code
    ) {
        final Long login = oAuthService.login(oAuthProviderType, code);
        return ResponseEntity.ok(login);
    }

    @GetMapping("token/expired")
    public boolean checkExpiredToken(final HttpServletRequest request) {
        final String authorization = request.getHeader(JwtHeader.AUTHORIZATION.getKey());
        if(!jwtTokenService.hasBearerToken(authorization)) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED, "잘못된 토큰 정보");
        }
        final String token = jwtTokenService.extractBearerToken(authorization);
        return jwtTokenService.verifyToken(token);
    }

    @GetMapping("token/refresh")
    public void refreshToken(final HttpServletRequest request, final HttpServletResponse response) {
        final String authorization = request.getHeader(JwtHeader.AUTHORIZATION.getKey());

        if(!jwtTokenService.hasBearerToken(authorization)) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED, "잘못된 토큰 정보");
        }

        final String token = jwtTokenService.extractBearerToken(authorization);
        final UserDto user = jwtTokenService.getUserFromToken(token);
        final JwtToken newJwtToken = jwtTokenService.generateToken(user);

        response.addHeader(JwtHeader.AUTHORIZATION.getKey(), newJwtToken.accessToken());
        response.addHeader(JwtHeader.AUTHORIZATION_REFRESH.getKey(), newJwtToken.refreshToken());
        response.setContentType(JwtHeader.CONTENT_TYPE.getKey());
    }

    // TODO: 로그아웃, 회원탈퇴 구현해야 함
}
