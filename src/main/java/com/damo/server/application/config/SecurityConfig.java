package com.damo.server.application.config;

import com.damo.server.application.config.oauth.OAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final OAuth2UserService oAuth2UserService;

    public SecurityConfig(OAuth2UserService oAuth2UserService) {
        this.oAuth2UserService = oAuth2UserService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        //.requestMatchers("/.../**").authenticated()
                        //.requestMatchers("/.../**").hasAnyRole("ADMIN", "MANAGER")
                        //.requestMatchers("/.../**").hasRole("ADMIN")
                        .anyRequest().permitAll() // 위 페이지 외 누구나 들어갈 수 있음
                )
                .logout(logout -> logout.logoutUrl("/api/logout") // 로그아웃
                        // .logoutSuccessUrl("/loginForm")  // TODO: 프론트 로그인 페이지 설정
                        .invalidateHttpSession(true)  // HTTP 세션 무효화 여부
                        .deleteCookies("JSESSIONID")  // 로그아웃 시 삭제할 쿠키 이름
                )
                .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(endPoint -> endPoint.userService(oAuth2UserService))
                        .defaultSuccessUrl("/") // TODO: 프론트 루트 경로로 이동
                )
                .build();
    }
}
