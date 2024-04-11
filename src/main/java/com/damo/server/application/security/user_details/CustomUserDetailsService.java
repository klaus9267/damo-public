package com.damo.server.application.security.user_details;

import com.damo.server.domain.common.exception.ExceptionThrowHelper;
import com.damo.server.domain.user.entity.User;
import com.damo.server.domain.user.repository.UserRepository;
import java.util.Collection;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * {@code CustomUserDetailsService} 클래스는 Spring Security의 {@code UserDetailsService}를
 * 구현한 사용자 정의 서비스 클래스입니다.
 * 이 클래스는 {@code UserRepository}를 통해 사용자 정보를 로드하고,
 * {@code CustomUserDetails} 객체로 변환하여 반환합니다.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public CustomUserDetails loadUserByUsername(final String username) {
    return userRepository.findOneByUsername(username)
        .map(this::createUserDetails)
        .orElseThrow(ExceptionThrowHelper.throwUnauthorized("유저 정보를 찾을 수 없습니다."));
  }

  private CustomUserDetails createUserDetails(final User user) {
    final Collection<? extends GrantedAuthority> authorities = Collections.singleton(
      new SimpleGrantedAuthority(user.getRole().getKey())
    );

    return new CustomUserDetails(
        user.getUsername(),
        user.getProviderId(),
        authorities,
        user.getId(),
        user.getName()
    );
  }
}

