package com.damo.server.application.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("oauth")
public class OAuthController {
    // OAuth 연동 확인을 위한 임시 API
    @GetMapping
    public Map<String, Object> tempOAuth2(@AuthenticationPrincipal OAuth2User oauth) {
        return oauth.getAttributes();
    }
}
