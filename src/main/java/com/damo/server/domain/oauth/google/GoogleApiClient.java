package com.damo.server.domain.oauth.google;

import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface GoogleApiClient {
    @PostExchange(url = "https://nid.naver.com/oauth2.0/token")
    GoogleToken fetchToken(@RequestParam final MultiValueMap<String, String> params);

    @GetExchange("https://openapi.naver.com/v1/nid/me")
    GoogleMemberResponse fetchMember(@RequestHeader(name = HttpHeaders.AUTHORIZATION) final String bearerToken);
}
