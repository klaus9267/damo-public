package com.damo.server.domain.person.entity;

import com.damo.server.domain.person.dto.RequestPersonDto;
import com.damo.server.domain.transaction.entity.Transaction;
import com.damo.server.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Getter
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String relation;

    @Column
    private String contact;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "person", cascade = CascadeType.REMOVE)
    private final List<Transaction> transactions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Person(final RequestPersonDto personDto, final Long userId) {
        this.name = personDto.name();
        this.contact = personDto.contact();
        this.relation = personDto.relation();
        this.memo = personDto.memo();
        this.user = User.builder().id(userId).build();
    }

    public static Person toPersonFromRequest(final RequestPersonDto personDto, final Long userId) {
        return new Person(personDto, userId);
    }

    public void changeInfo(final RequestPersonDto personDto) {
        this.name = personDto.name() != null ? personDto.name() : getName();
        this.contact = personDto.contact() != null ? personDto.contact() : getContact();
        this.relation = personDto.relation() != null ? personDto.relation() : getRelation();
        this.memo = personDto.memo() != null ? personDto.memo() : getMemo();
    }
}
