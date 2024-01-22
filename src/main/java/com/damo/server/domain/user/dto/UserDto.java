package com.damo.server.domain.user.dto;

import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import com.damo.server.domain.user.UserRole;
import com.damo.server.domain.user.entity.User;
import lombok.Getter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@ToString
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String username;
    private UserRole role;
    private OAuthProviderType providerType;
    private String providerId;
    private Timestamp createdAt;

    // 토큰 전용 생성자
    public UserDto(final Long id, final String email, final UserRole userRole) {
        this.id = id;
        this.email = email;
        this.role = userRole;
    }

    private UserDto(final User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.role = user.getRole();
        this.providerType = user.getProviderType();
        this.providerId = user.getProviderId();
        this.createdAt = user.getCreatedAt();
    }

    static public UserDto from(final User user) {
        return new UserDto(user);
    }
}
