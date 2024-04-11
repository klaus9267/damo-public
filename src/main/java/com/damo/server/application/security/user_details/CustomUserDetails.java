package com.damo.server.application.security.user_details;

import java.util.Collection;
import java.util.Objects;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * {@code CustomUserDetails} 클래스는 Spring Security의 {@code User} 클래스를 상속받아
 * 사용자의 상세 정보를 나타내는 클래스입니다.
 * 이 클래스는 추가적인 사용자 정보인 id와 name을 가지고 있습니다.
 */
@Getter
public class CustomUserDetails extends User {
  private final Long id;
  private final String name;

  /**
   * {@code CustomUserDetails}의 생성자입니다.
   *
   * @param username 사용자의 식별자(username)
   * @param providerId 사용자의 고유한 공급자 식별자
   * @param authorities 사용자의 권한 목록
   * @param id 사용자의 고유한 식별자(id)
   * @param name 사용자의 이름
   */
  public CustomUserDetails(
      final String username,
      final String providerId,
      final Collection<? extends GrantedAuthority> authorities,
      final Long id,
      final String name
  ) {
    super(username, providerId, authorities);
    this.id = id;
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    CustomUserDetails that = (CustomUserDetails) o;
    return Objects.equals(id, that.id) && Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id, name);
  }
}
