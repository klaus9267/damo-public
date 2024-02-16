package com.damo.server.domain.user.entity;

import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import com.damo.server.domain.person.entity.Person;
import com.damo.server.domain.user.UserRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

/**
 * `User` 엔터티 클래스는 사용자 정보를 나타내는 엔터티입니다.
 */
@Getter
@Data
@NoArgsConstructor
@Entity(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private UserRole role;

  @Column(name = "profile_image_url")
  private String profileImageUrl;

  @Column(name = "provider_id", nullable = false)
  private String providerId;

  @Column(name = "provider_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private OAuthProviderType providerType;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<Person> people = new ArrayList<>();

  /**
   * 빌더 패턴을 사용하여 `User` 객체를 생성하는 생성자입니다.
   *
   * @param id             사용자 식별자
   * @param name           사용자 이름
   * @param email          사용자 이메일
   * @param role           사용자 역할
   * @param username       사용자 아이디
   * @param profileImageUrl 사용자 프로필 이미지 URL
   * @param providerType   OAuth 공급자 타입
   * @param providerId     OAuth 공급자 식별자
   */
  @Builder
  public User(
      final Long id,
      final String name,
      final String email,
      final UserRole role,
      final String username,
      final String profileImageUrl,
      final OAuthProviderType providerType,
      final String providerId
  ) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.role = role;
    this.username = username;
    this.profileImageUrl = profileImageUrl;
    this.providerType = providerType;
    this.providerId = providerId;
  }

  public void changeProviderId(final String providerId) {
    this.providerId = providerId;
  }

}
