package com.damo.server.application.security.kakao;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * Kakao API와 상호작용하기 위한 클라이언트 인터페이스입니다.
 */
public interface KakaoApiClient {
  @PostExchange(url = "https://kauth.kakao.com/oauth/token", contentType = APPLICATION_FORM_URLENCODED_VALUE)
  KakaoToken fetchToken(
      @RequestParam("params") final MultiValueMap<String, String> params
  );

  @GetExchange("https://kapi.kakao.com/v2/user/me")
  KakaoUserResponse fetchMember(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) final String bearerToken
  );
}