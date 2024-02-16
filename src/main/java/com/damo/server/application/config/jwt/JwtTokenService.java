package com.damo.server.application.config.jwt;

import com.damo.server.application.config.user_details.CustomUserDetails;
import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * JWT 토큰 생성, 유효성 검사 및 관련 기능을 제공합니다.
 * 서비스 초기화 시 시크릿 키와 토큰 만료 시간을 설정하며, 사용자 인증 정보를 기반으로 액세스 및 리프레시 토큰을 생성합니다.
 * 또한 토큰에서 권한 정보를 추출하여 사용자 인증 객체를 생성하고 유효성을 검사합니다.
 * 토큰 해석 및 "Bearer " 제거와 같은 편의 기능도 제공합니다.
 * 이 모든 기능은 로깅과 예외 처리를 통해 안전하게 구현되었습니다.
 */
@Slf4j
@Service
public class JwtTokenService {
  private static final String AUTHORITIES_KEY = "authorities";
  private final SecretKey secretKey;
  private final Long accessTokenExpiredTime;
  private final Long refreshTokenExpiredTime;

  /**
   * 시크릿 키와 토큰 만료 시간을 설정값에서 받아오고, Base64 디코딩 후 SecretKeySpec 초기화합니다.
   * JWT 토큰의 생성 및 유효성 검증을 위한 초기화가 이루어집니다.
   */
  public JwtTokenService(
      @Value("${jwt.secret}") final String secretKey,
      @Value("${jwt.access-token-expired-time}") final Long accessTokenExpiredTime,
      @Value("${jwt.refresh-token-expired-time}") final Long refreshTokenExpiredTime
  ) {
    final byte[] decodedKey = Base64.getEncoder().encode(secretKey.getBytes());
    this.secretKey = new SecretKeySpec(
        decodedKey,
        0,
        decodedKey.length,
        SignatureAlgorithm.HS256.getJcaName()
    );
    this.accessTokenExpiredTime = accessTokenExpiredTime;
    this.refreshTokenExpiredTime = refreshTokenExpiredTime;
  }

  /**
   * 사용자 인증 정보 및 권한을 기반으로 JWT 액세스 및 리프레시 토큰을 생성합니다.
   * Jwts.builder()를 사용하여 사용자 정보를 포함하고 만료 시간을 설정합니다.
   * 생성된 액세스 및 리프레시 토큰이 담긴 JwtToken 객체를 반환합니다.
   */
  public JwtToken generateToken(final Authentication authentication) {
    return JwtToken.builder()
        .accessToken(buildJwtTokenWithExpiredTime(authentication, accessTokenExpiredTime))
        .refreshToken(buildJwtTokenWithExpiredTime(authentication, refreshTokenExpiredTime))
        .build();
  }

  private String buildJwtTokenWithExpiredTime(
      final Authentication authentication,
      final Long expiredTime
  ) {
    final Date expiredDate = new Date(new Date().getTime() + expiredTime);
    final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    final String authorities = authentication
        .getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    return Jwts.builder()
        .setSubject(authentication.getName())
        .claim("id", userDetails.getId())
        .claim("name", userDetails.getName())
        .claim("username", userDetails.getUsername())
        .claim(AUTHORITIES_KEY, authorities)
        .setExpiration(expiredDate)
        .signWith(secretKey)
        .compact();
  }

  /**
   * 주어진 토큰에서 권한 정보를 추출하여 사용자 인증 객체를 생성하고 반환하는 메서드입니다.
   * 토큰에 권한 정보가 없으면 "UNAUTHORIZED" 예외가 발생합니다.
   * 사용자 정보와 권한을 포함한 CustomUserDetails 객체를 생성하여 UsernamePasswordAuthenticationToken 반환합니다.
   */
  public Authentication getAuthentication(final String token) {
    final Claims claims = extractClaims(token);

    if (claims.get(AUTHORITIES_KEY) == null) {
      throw new CustomException(CustomErrorCode.UNAUTHORIZED, "유저 인증 정보가 없습니다.");
    }
    final Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .toList();

    final CustomUserDetails userDetails = new CustomUserDetails(
        claims.getSubject(),
        "",
        authorities,
        Long.valueOf(claims.get("id").toString()),
        claims.get("name").toString()
    );

    return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
  }

  /**
   * 주어진 토큰을 유효성 검사하고, 유효하면 true, 아니면 false 반환합니다. 검사 실패 시 로깅됩니다.
   */
  public boolean validateToken(final String token) {
    try {
      Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
      return !checkIfTokenExpired(token);
    } catch (final Exception e) {
      log.error(Arrays.toString(e.getStackTrace()));
    }
    return false;
  }

  private Claims extractClaims(final String token) {
    try {
      return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    } catch (final ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  private Boolean checkIfTokenExpired(String token) {
    final Date expirationDate = extractExpiration(token);
    return expirationDate.before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaims(token).getExpiration();
  }

  /**
   * 주어진 토큰에서 "Bearer "를 제거한 토큰을 추출합니다.
   */
  public String resolveToken(final String token) {
    return StringUtils.hasText(token) && token.startsWith("Bearer ") ? token.substring(7) : null;
  }
}

