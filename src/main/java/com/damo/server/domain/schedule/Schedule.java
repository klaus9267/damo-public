package com.damo.server.domain.schedule;

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

    private LocalDateTime date;

    @ColumnDefault("0")
    private Integer amount;

    @Column(columnDefinition = "TEXT")
    private String memo;

    private Long personId; // TODO: 관계 매핑해야 함
    private String status; // TODO: 나간 돈, 받은 돈, 진행 예정 등 enum타입

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;
}
