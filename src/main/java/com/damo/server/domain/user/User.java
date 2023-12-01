package com.damo.server.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username; // 고유 식별값

    private String name;
    private String email;
    private String role;
    private String provider; // TODO: enum으로 변경 / google, kakao, naver
    private String providerId;

    @CreationTimestamp
    private Timestamp createdDate;


    @Builder
    public User(String username, String name, String email, String role, String provider, String providerId, Timestamp createdDate) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.createdDate = createdDate;
    }
}
