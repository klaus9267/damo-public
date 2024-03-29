package com.damo.server.application.security.google;

import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * Google API와 상호작용하기 위한 클라이언트 인터페이스입니다.
 */
public interface GoogleApiClient {
  @PostExchange(url = "https://oauth2.googleapis.com/token")
  GoogleToken fetchToken(
      @RequestParam("params") final MultiValueMap<String, String> params
  );

  @GetExchange("https://www.googleapis.com/oauth2/v2/userinfo")
  GoogleUserResponse fetchMember(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) final String bearerToken
  );
}
