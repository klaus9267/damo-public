package com.damo.server.domain.schedule;

import com.damo.server.domain.schedule.dto.RequestCreateScheduleDto;
import com.damo.server.domain.transaction.dto.RequestCreateTransactionDto;
import com.damo.server.domain.transaction.entity.Transaction;
import com.damo.server.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
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
    private String event;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Enumerated(EnumType.STRING)
    //    @Column(nullable = false)
    // 임시 null 허용 후에 사용 시 주석 해제
    private ScheduleStatus status;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    public Schedule(final RequestCreateTransactionDto transactionDto, final Transaction transaction) {
        this.event = transactionDto.event();
        this.eventDate = transactionDto.eventDate();
        this.transaction = transaction;
    }

    public Schedule(final RequestCreateScheduleDto scheduleDto, final Long userId) {
        this.event = scheduleDto.event();
        this.eventDate = scheduleDto.eventDate();
        this.status = scheduleDto.status();
        this.memo = scheduleDto.memo();
        this.user = User.builder().id(userId).build();
    }

    public static Schedule from(final RequestCreateTransactionDto transactionDto, final Transaction transaction) {
        return new Schedule(transactionDto, transaction);
    }

    public static Schedule from(final RequestCreateScheduleDto scheduleDto, final Long userId) {
        return new Schedule(scheduleDto, userId);
    }
}
