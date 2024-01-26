package com.damo.server.domain.user.entity;


import jakarta.persistence.*;
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

  @Column(name = "refresh_token", nullable = false)
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
