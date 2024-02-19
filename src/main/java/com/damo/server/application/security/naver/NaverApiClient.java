package com.damo.server.application.security.naver;

import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * Naver API와 상호작용하기 위한 클라이언트 인터페이스입니다.
 */
public interface NaverApiClient {
  @PostExchange(url = "https://nid.naver.com/oauth2.0/token")
  NaverToken fetchToken(
      @RequestParam("params") final MultiValueMap<String, String> params
  );

  @GetExchange("https://openapi.naver.com/v1/nid/me")
  NaverUserResponse fetchMember(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) final String bearerToken
  );
}
