package com.damo.server.domain.user.entity;

import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import com.damo.server.domain.person.entity.Person;
import com.damo.server.domain.user.UserRole;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Data
@NoArgsConstructor
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "profile_url", nullable = false)
    private String profileUrl;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Column(name = "provider_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OAuthProviderType providerType;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @Builder
    public User(
            final Long id,
            final String name,
            final String email,
            final UserRole role,
            final String username,
            final String profileUrl,
            final OAuthProviderType providerType,
            final String providerId
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.username = username;
        this.profileUrl = profileUrl;
        this.providerType = providerType;
        this.providerId = providerId;
    }

    public void changeProviderId(final String providerId) {
        this.providerId = providerId;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.DETACH)
    private final List<Person> people = new ArrayList<>();
}
