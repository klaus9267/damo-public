package com.damo.server.application.security.user_details;

import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * {@code SecurityUserUtil} 클래스는 Spring Security의 보안 컨텍스트에서 현재 인증된 사용자의 정보를
 * 추출하여 제공하는 유틸리티 클래스입니다.
 */
@Component
public class SecurityUserUtil {
  public Long getId() {
    return getCustomUserDetails().getId();
  }

  public String getName() {
    return getCustomUserDetails().getName();
  }

  public String getUsername() {
    return getCustomUserDetails().getUsername();
  }

  private CustomUserDetails getCustomUserDetails() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication.getName() == null) {
      throw new CustomException(CustomErrorCode.UNAUTHORIZED, "유저 정보를 찾을 수 없습니다.");
    }

    return (CustomUserDetails) authentication.getPrincipal();
  }
}

