package com.damo.server.application.config;

import com.damo.server.application.config.new_oauth.OAuthProviderTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("/*")    // 외부에서 들어오는 모둔 url 을 허용
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.PATCH.name()
                )
                .allowedHeaders("*")    // 허용되는 헤더
                .allowCredentials(true)    // 자격증명 허용
                .maxAge(3600);   // 허용 시간
    }

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addConverter(new OAuthProviderTypeConverter());
    }
}
