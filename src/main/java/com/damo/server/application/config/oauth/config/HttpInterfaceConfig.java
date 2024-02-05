package com.damo.server.application.config.oauth.config;

import com.damo.server.application.config.oauth.google.GoogleApiClient;
import com.damo.server.application.config.oauth.kakao.KakaoApiClient;
import com.damo.server.application.config.oauth.naver.NaverApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * HTTP 인터페이스 관련 빈을 정의하는 Spring Configuration 클래스입니다.
 * 각 HTTP 클라이언트를 빈으로 등록하고, 필요한 경우 해당 빈을 주입하여 사용할 수 있도록 합니다.
 */
@Configuration
public class HttpInterfaceConfig {
  @Bean
  public KakaoApiClient kakaoApiClient() {
    return createHttpInterface(KakaoApiClient.class);
  }

  @Bean
  public NaverApiClient naverApiClient() {
    return createHttpInterface(NaverApiClient.class);
  }

  @Bean
  public GoogleApiClient googleApiClient() {
    return createHttpInterface(GoogleApiClient.class);
  }

  private <T> T createHttpInterface(final Class<T> clazz) {
    final WebClient webClient = WebClient.create();
    final HttpServiceProxyFactory build = HttpServiceProxyFactory
        .builderFor(WebClientAdapter.create(webClient))
        .build();

    return build.createClient(clazz);
  }
}
