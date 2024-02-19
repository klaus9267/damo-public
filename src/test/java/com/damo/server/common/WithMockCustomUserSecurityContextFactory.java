package com.damo.server.common;

import com.damo.server.application.security.user_details.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collection;
import java.util.Collections;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
  
  @Override
  public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
    final SecurityContext context = SecurityContextHolder.createEmptyContext();
    final Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(annotation.role().getKey()));
    final CustomUserDetails userDetails = new CustomUserDetails(
        annotation.username(),
        "test providerId",
        authorities,
        1L,
        "test name"
    );
    final Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    context.setAuthentication(auth);
    return context;
  }
}
