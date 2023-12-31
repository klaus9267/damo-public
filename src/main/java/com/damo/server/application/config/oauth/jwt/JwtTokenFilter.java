package com.damo.server.application.config.oauth.jwt;

import com.damo.server.domain.user.UserRole;
import com.damo.server.domain.user.dto.UserDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {
    private final JwtTokenService jwtTokenService;

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final String authorization = ((HttpServletRequest) request).getHeader(JwtHeader.AUTHORIZATION.getKey());
        final String token = jwtTokenService.hasBearerToken(authorization) ? jwtTokenService.extractBearerToken(authorization) : null;

        if (token != null && jwtTokenService.verifyToken(token)) {
            final UserDto user = jwtTokenService.getUserFromToken(token);
            final Authentication auth = getAuthentication(user);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }

    public Authentication getAuthentication(final UserDto user) {
        return new UsernamePasswordAuthenticationToken(
                user,
                "",
                List.of(new SimpleGrantedAuthority(UserRole.USER.getKey()))
        );
    }
}
