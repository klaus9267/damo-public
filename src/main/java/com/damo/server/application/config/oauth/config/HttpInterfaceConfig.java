package com.damo.server.application.config.oauth.config;

import com.damo.server.application.config.oauth.google.GoogleApiClient;
import com.damo.server.application.config.oauth.kakao.KakaoApiClient;
import com.damo.server.application.config.oauth.naver.NaverApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
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
