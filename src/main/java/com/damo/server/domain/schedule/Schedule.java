package com.damo.server.domain.schedule;

import com.damo.server.domain.person.Person;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false, columnDefinition = "0")
    private Integer amount;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Column(nullable = false)
    private String event; // TODO: enum 타입 수정

//    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private String status; // TODO: 나간 돈, 받은 돈, 진행 예정 등 enum타입

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
}
