package com.damo.server.domain.oauth;

import com.damo.server.domain.oauth.kakao.KakaoApiClient;
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

    private <T> T createHttpInterface(final Class<T> clazz) {
        final WebClient webClient = WebClient.create();
        final HttpServiceProxyFactory build = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build();

        return build.createClient(clazz);
    }
}
