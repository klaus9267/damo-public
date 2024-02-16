package com.damo.server.application.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@code SwaggerConfig} 클래스는 Swagger 설정을 담당하는 클래스입니다.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@OpenAPIDefinition(
    info = @Info(
        title = "DAMO API",
        description = "Damo's API",
        version = "v0.1.0"
    ),
    security = @SecurityRequirement(name = "bearer-token")
)
@SecuritySchemes({
    @SecurityScheme(
        name = "bearer-token",
        type = SecuritySchemeType.HTTP,
        description = "Header param - Authorization",
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        paramName = "Authorization"
    ),
})
@Configuration
public class SwaggerConfig {
  /**
   * Swagger의 그룹을 설정하는 메서드입니다.
   *
   * @return 설정된 Swagger 그룹
   */
  @Bean
  public GroupedOpenApi openAPI() {
    String[] paths = {"/api/**", "/oauth/**"};
    return GroupedOpenApi.builder()
        .group("DAMO API")
        .pathsToMatch(paths)
        .build();
  }
}