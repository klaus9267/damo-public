package com.damo.server.application.config.oauth.jwt;

import com.damo.server.domain.user.UserRole;
import com.damo.server.domain.user.dto.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

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

    public JwtToken generateToken(final UserDto user) {
        final Claims claims = Jwts.claims().setSubject(user.getId().toString());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole().getKey());

        final String accessToken = buildToken(claims, ACCESS_TOKEN_EXPIRED_TIME);
        final String refreshToken = buildToken(claims, REFRESH_TOKEN_EXPIRED_TIME);

        return new JwtToken(accessToken, refreshToken);
    }

    public boolean verifyToken(final String token) {
        try {
            final Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return claims.getBody()
                    .getExpiration()
                    .after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public UserDto getUserFromToken(final String token) {
        final Claims claimsBody = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        final Long id =  Long.valueOf(claimsBody.getSubject());
        final String email = String.valueOf(claimsBody.get("email"));
        final String claimsRole = String.valueOf(claimsBody.get("role"));

        UserRole role = null;
        for (UserRole userRole : UserRole.values()) {
            if (userRole.getKey().equals(claimsRole)) {
                role = userRole;
                break;
            }
        }

        return new UserDto(id, email, role);
    }

    private String buildToken(final Claims claims, final Long expiredTime) {
        final Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiredTime))
                .signWith(secretKey)
                .compact();
    }
}