package com.damo.server.application.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "DAMO API",
                description = "Damo's API",
                version = "v0.1.0")
)
@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi openAPI() {
        String[] paths = {"/api/**"};
        return GroupedOpenApi.builder()
                .group("DAMO API")
                .pathsToMatch(paths)
                .build();
    }
}