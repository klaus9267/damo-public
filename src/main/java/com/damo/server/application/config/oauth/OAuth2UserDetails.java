package com.damo.server.application.config.oauth;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class OAuth2UserDetails implements OAuth2User {
    private final Map<String, Object> attributes;

    public OAuth2UserDetails(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() { // 크게 중요하지 않음, 사용을 잘 안해서
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add((GrantedAuthority) () -> "user.getRole()");
        return collection;
    }
}
