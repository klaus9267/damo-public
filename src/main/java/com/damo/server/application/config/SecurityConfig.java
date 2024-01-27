package com.damo.server.application.config;

import com.damo.server.application.config.jwt.JwtAuthenticationEntryPoint;
import com.damo.server.application.config.jwt.JwtAuthenticationFilter;
import com.damo.server.application.config.jwt.JwtTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
  private final JwtTokenService jwtTokenService;
  private final ObjectMapper objectMapper;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
      .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer
        .configurationSource(corsConfigurationSource())
      )
      .csrf(CsrfConfigurer::disable)
      .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer
          .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
      )
      .logout(AbstractHttpConfigurer::disable)
      .formLogin(AbstractHttpConfigurer::disable)
      .sessionManagement(session -> session
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 쿠키 대신 토큰 사용
      )
      .authorizeHttpRequests(authorize -> authorize
          .requestMatchers("/oauth/**", "/v3/**", "/swagger-ui/**").permitAll() // swagger 허용
          .requestMatchers("/**").hasAnyRole("USER", "ADMIN")
      )
      .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer
        .authenticationEntryPoint(new JwtAuthenticationEntryPoint(jwtTokenService, objectMapper))
      )
      .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
      .build();
  }

  private CorsConfigurationSource corsConfigurationSource() {
    return request -> {
      final CorsConfiguration config = new CorsConfiguration();

      config.setAllowedHeaders(Collections.singletonList("*"));
      config.setAllowedMethods(Collections.singletonList("*"));
      // TODO: 환경변수 cors 경로 추가한 후 적용해야 함
      config.setAllowedOriginPatterns(Collections.singletonList("*"));
      config.setAllowCredentials(true);

      return config;
    };
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      final AuthenticationConfiguration authenticationConfiguration
  ) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}
