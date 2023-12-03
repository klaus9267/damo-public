package com.damo.server.domain.person;

import com.damo.server.domain.person.dto.RequestPersonDto;
import com.damo.server.domain.schedule.Schedule;
import com.damo.server.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String relation;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @OneToMany(mappedBy = "person", cascade = CascadeType.REMOVE)
    private final List<Schedule> schedules = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Person(final RequestPersonDto personDto) {
        this.name = personDto.name();
        this.relation = personDto.relation();
        this.memo = personDto.memo();
        this.user = User.builder().id(personDto.userId()).build();
    }

    public static Person toPersonFromRequest(final RequestPersonDto personDto) {
        return new Person(personDto);
    }
}
