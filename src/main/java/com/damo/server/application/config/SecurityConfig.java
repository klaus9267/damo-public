package com.damo.server.application.config;

import com.damo.server.application.config.oauth.OAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
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
                        .requestMatchers("/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/v3/**", "/swagger-ui/**") // swagger 허용
                        .permitAll()
                )
                .logout(logout -> logout.logoutUrl("/api/logout") // 로그아웃
                        // .logoutSuccessUrl("/loginForm")  // TODO: 프론트 로그인 페이지 설정
                        .invalidateHttpSession(true)  // HTTP 세션 무효화 여부
                        .deleteCookies("JSESSIONID")  // 로그아웃 시 삭제할 쿠키 이름
                )
                .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(endPoint -> endPoint.userService(oAuth2UserService))
                        .defaultSuccessUrl("/oauth") // TODO: 프론트 루트 경로로 이동
                )
                .httpBasic(withDefaults())
                .build();
    }
}
