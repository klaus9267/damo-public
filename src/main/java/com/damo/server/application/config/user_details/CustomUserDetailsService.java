package com.damo.server.application.config.user_details;

import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import com.damo.server.domain.user.entity.User;
import com.damo.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(final String providerId) {
        return userRepository.findOneByProviderId(providerId)
                .map(this::createUserDetails)
                .orElseThrow(() -> new CustomException(CustomErrorCode.UNAUTHORIZED, "유저 정보를 찾을 수 없습니다."));
    }

    private CustomUserDetails createUserDetails(final User user) {
        final Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().getKey()));

        return new CustomUserDetails(user.getUsername(), user.getProviderId(), authorities, user.getId(), user.getName());
    }
}

