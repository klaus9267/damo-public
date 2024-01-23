package com.damo.server.application.controller;

import com.damo.server.application.config.jwt.JwtToken;
import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import com.damo.server.application.config.oauth.OAuthService;
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
    private final OAuthService oAuthService;

    @SneakyThrows
    @GetMapping("{oAuthProviderType}")
    public ResponseEntity<Void> redirectAuthCodeRequestUrl(
            @PathVariable("oAuthProviderType") final OAuthProviderType oAuthProviderType,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        final boolean isDev = checkIfDevelopEnvironment(request.getRequestURL().toString());
        final String redirectUrl = oAuthService.getAuthCodeRequestUrl(oAuthProviderType, isDev);

        response.sendRedirect(redirectUrl);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/{oAuthProviderType}")
    public ResponseEntity<?> login(
            @PathVariable("oAuthProviderType") final OAuthProviderType oAuthProviderType,
            @RequestParam("code") final String code,
            final HttpServletRequest request
    ) {
        final boolean isDev = checkIfDevelopEnvironment(request.getRequestURL().toString());
        final JwtToken jwtToken = oAuthService.login(oAuthProviderType, code, isDev);

        return ResponseEntity.ok(jwtToken);
    }

    private boolean checkIfDevelopEnvironment(final String requestUrl) {
        return requestUrl.contains("localhost") || requestUrl.contains("127.0.0.1");
    }
}
