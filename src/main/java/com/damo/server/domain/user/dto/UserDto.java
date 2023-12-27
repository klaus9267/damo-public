package com.damo.server.domain.user.dto;

import com.damo.server.application.config.oauth.provider.ProviderType;
import com.damo.server.domain.user.UserRole;
import com.damo.server.domain.user.entity.User;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class UserDto {
    private Long id;
    private String username; // 고유 식별값
    private String name;
    private String email;
    private UserRole role;
    private ProviderType provider;
    private String providerId;
    private Timestamp createdAt;

    private UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.provider = user.getProvider();
        this.providerId = user.getProviderId();
        this.createdAt = user.getCreatedAt();
    }

    static public UserDto toUserDto(User user) {
        return new UserDto(user);
    }
}
