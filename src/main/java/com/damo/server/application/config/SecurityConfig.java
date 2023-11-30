package com.damo.server.application.config;

import com.damo.server.application.config.oauth.OAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private OAuth2UserService oAuth2UserService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        //.requestMatchers("/.../**").authenticated()
                        //.requestMatchers("/.../**").hasAnyRole("ADMIN", "MANAGER")
                        //.requestMatchers("/.../**").hasRole("ADMIN")
                        .anyRequest().permitAll() // 위 페이지 외 누구나 들어갈 수 있음
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // 로그아웃 URL을 설정 (기본값은 /logout)
                        .logoutSuccessUrl("/loginForm")  // 로그아웃 성공 시 이동할 페이지 설정
                        .invalidateHttpSession(true)  // HTTP 세션 무효화 여부
                        .deleteCookies("JSESSIONID")  // 로그아웃 시 삭제할 쿠키 이름
                )
//                .formLogin(Customizer.withDefaults());  // 기본 로그인 폼 사용;
                .formLogin((customizer) -> customizer
                                .loginPage("/loginForm")  // 커스텀 로그인 폼 사용
//                    .usernameParameter() 커스텀 username 파라미터 등록방법
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/") // 성공시 이동하는 페이지
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/loginForm")
                        .userInfoEndpoint(endPoint -> endPoint
                                .userService(oAuth2UserService)
                        )
                );
        return http.build();
    }
}
