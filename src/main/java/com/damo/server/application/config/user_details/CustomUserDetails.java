package com.damo.server.application.config.user_details;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUserDetails extends User {
    private final Long id;
    private final String name;

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
}
