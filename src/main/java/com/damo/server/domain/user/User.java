package com.damo.server.domain.user;

import com.damo.server.domain.person.Person;
import com.damo.server.domain.schedule.Schedule;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username; // 고유 식별값

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String role; // TODO: enum으로 변경 / ROLE_USER, ROLE_ADMIN 등

    @Column(nullable = false)
    private String provider; // TODO: enum으로 변경 / google, kakao, naver

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @Builder
    public User(String username, String name, String email, String role, String provider, String providerId) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.DETACH)
    private final List<Person> people = new ArrayList<>();
}
