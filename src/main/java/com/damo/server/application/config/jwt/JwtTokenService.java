package com.damo.server.application.config.jwt;


import com.damo.server.application.config.user_details.CustomUserDetails;
import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtTokenService {
    private final SecretKey secretKey;
    private final Long ACCESS_TOKEN_EXPIRED_TIME;
    private final Long REFRESH_TOKEN_EXPIRED_TIME;

    public JwtTokenService(
            @Value("${jwt.secret}") final String secretKey,
            @Value("${jwt.access-token-expired-time}") final Long accessTokenExpiredTime,
            @Value("${jwt.refresh-token-expired-time}") final Long refreshTokenExpiredTime
    ) {
        final byte[] decodedKey = Base64.getEncoder().encode(secretKey.getBytes());
        this.secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, SignatureAlgorithm.HS256.getJcaName());
        this.ACCESS_TOKEN_EXPIRED_TIME = accessTokenExpiredTime;
        this.REFRESH_TOKEN_EXPIRED_TIME = refreshTokenExpiredTime;
    }

    public JwtToken generateToken(final Authentication authentication) {
        final String authorities = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        final Long id = userDetails.getId();
        final String name = userDetails.getName();

        final String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("id", id)
                .claim("name", name)
                .claim("authorities", authorities)
                .setExpiration(new Date(ACCESS_TOKEN_EXPIRED_TIME))
                .signWith(secretKey)
                .compact();

        final String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("id", id)
                .claim("name", name)
                .claim("authorities", authorities)
                .setExpiration(new Date(REFRESH_TOKEN_EXPIRED_TIME))
                .signWith(secretKey)
                .compact();

        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(final String token) {
        final Claims claims = parseClaims(token);

        if(claims.get("authorities") == null) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED, "유저 인증 정보가 없습니다.");
        }

        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("authorities").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        final CustomUserDetails userDetails = new CustomUserDetails(
                claims.getSubject(),
                "",
                authorities,
                Long.valueOf(claims.get("id").toString()),
                claims.get("name").toString()
        );

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    public boolean validateToken(final String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Claims parseClaims(final String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        } catch (final ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String resolveToken(final String token) {
        final String prefix = "Bearer ";
        if(StringUtils.hasText(token) && token.startsWith(prefix)) {
            return token.substring(prefix.length());
        }
        return null;
    }
}

