package com.damo.server.application.config;

import com.damo.server.application.config.oauth.jwt.JwtTokenFilter;
import com.damo.server.application.config.oauth.OAuth2SuccessHandler;
import com.damo.server.application.config.oauth.OAuth2UserService;
import com.damo.server.application.config.oauth.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final OAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler successHandler;
    private final JwtTokenService jwtTokenService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(CsrfConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 쿠키 대신 토큰 사용
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/v3/**", "/swagger-ui/**", "/oauth/token/**").permitAll() // swagger 허용
                        .requestMatchers("/**").hasAnyRole("USER", "ADMIN")
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(successHandler)
                        .userInfoEndpoint(endPoint -> endPoint.userService(oAuth2UserService))
                )
                .addFilterBefore(new JwtTokenFilter(jwtTokenService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
