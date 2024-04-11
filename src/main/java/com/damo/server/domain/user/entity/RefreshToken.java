package com.damo.server.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 추후 Redis 도입되면 DB 저장을 Redis로 변경.
 */
@Getter
@Data
@NoArgsConstructor
@Entity(name = "refresh_tokens")
public class RefreshToken {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "refresh_token", nullable = false, length = 500) // TODO: 추후 토큰 길이 다시 고려해봐야 함
  private String token;

  @Column(unique = true)
  private String username;

  public RefreshToken(final String token, final String username) {
    this.token = token;
    this.username = username;
  }

  public void changeRefreshToken(final String refreshToken) {
    this.token = refreshToken;
  }
}
