package com.damo.server.domain.schedule.entity;

import com.damo.server.domain.person.Person;
import com.damo.server.domain.schedule.dto.RequestScheduleDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    private Schedule(final RequestScheduleDto scheduleDto) {
        this.person = Person.builder().id(scheduleDto.personId()).build();
        this.date = scheduleDto.date();
        this.amount = scheduleDto.amount();
        this.memo = scheduleDto.memo();
        this.event = scheduleDto.event();
        this.status = scheduleDto.status();
        this.transaction = scheduleDto.transaction();
    }

    public static Schedule from(final RequestScheduleDto scheduleDto) {
        return new Schedule(scheduleDto);
    }

    public void changeInfo(final RequestScheduleDto scheduleDto) {
        this.setDate(scheduleDto.date() != null ? scheduleDto.date() : this.getDate());
        this.setAmount(scheduleDto.amount() != null ? scheduleDto.amount() : this.getAmount());
        this.setMemo(scheduleDto.memo() != null ? scheduleDto.memo() : this.getMemo());
        this.setEvent(scheduleDto.event() != null ? scheduleDto.event() : this.getEvent());
        this.setStatus(scheduleDto.status() != null ? scheduleDto.status() : this.getStatus());
        this.setTransaction(scheduleDto.transaction() != null ? scheduleDto.transaction() : this.getTransaction());
    }
}
