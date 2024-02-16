package com.damo.server.application.config;

import com.damo.server.application.config.oauth.OAuthProviderTypeConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * {@code WebMvcConfig} 클래스는 Spring Web MVC 설정을 담당하는 클래스입니다.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    // TODO: 추후 배포시 cors 설정 제대로 검토해야 함
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
    
  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper().registerModule(new JavaTimeModule());
  }
}
