package com.damo.server.domain.user.repository;

import com.damo.server.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * `User` 엔터티에 대한 데이터 액세스를 처리하는 Spring Data JPA 리포지토리입니다.
 */
public interface UserRepository extends JpaRepository<User, Long> {
  /**
   * 주어진 사용자 아이디에 대한 `User` 엔터티를 조회하는 메서드입니다.
   *
   * @param username 조회할 사용자 아이디
   * @return 해당 사용자 아이디에 대한 `User` 엔터티 (존재하지 않을 경우 비어있는 Optional)
   */
  Optional<User> findOneByUsername(final String username);
}
