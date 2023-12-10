package com.damo.server.domain.schedule;

import com.damo.server.domain.person.Person;
import com.damo.server.domain.person.dto.PersonDto;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer amount; // Money

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Column(nullable = false)
    private String event; // 경조사 종류

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleTransaction transaction;

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
