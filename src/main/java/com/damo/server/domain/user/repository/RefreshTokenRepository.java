package com.damo.server.domain.user.repository;

import com.damo.server.domain.user.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * `RefreshToken` 엔터티에 대한 데이터 액세스를 처리하는 Spring Data JPA 리포지토리입니다.
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  /**
   * 주어진 사용자 아이디에 대한 `RefreshToken`을 조회하는 메서드입니다.
   *
   * @param username 조회할 사용자 아이디
   * @return 해당 사용자 아이디에 대한 `RefreshToken` (존재하지 않을 경우 비어있는 Optional)
   */
  Optional<RefreshToken> findOneByUsername(final String username);
}
