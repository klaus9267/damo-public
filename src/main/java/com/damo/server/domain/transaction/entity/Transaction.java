package com.damo.server.domain.transaction.entity;

import com.damo.server.domain.person.entity.Person;
import com.damo.server.domain.schedule.Schedule;
import com.damo.server.domain.transaction.dto.RequestCreateTransactionDto;
import com.damo.server.domain.transaction.dto.RequestUpdateTransactionDto;
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
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer amount; // Money

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionAction action;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    private Transaction(final RequestCreateTransactionDto scheduleDto, final Long userId) {
        this.person = Person.builder().id(scheduleDto.personId()).build();
        this.eventDate = scheduleDto.eventDate();
        this.amount = scheduleDto.amount();
        this.memo = scheduleDto.memo();
        this.action = scheduleDto.action();
        this.user = User.builder().id(userId).build();
    }

    public static Transaction from(final RequestCreateTransactionDto scheduleDto, final Long userId) {
        return new Transaction(scheduleDto, userId);
    }

    public void changeInfo(final RequestUpdateTransactionDto scheduleDto) {
        this.eventDate = scheduleDto.eventDate() != null ? scheduleDto.eventDate() : this.getEventDate();
        this.amount = scheduleDto.amount() != null ? scheduleDto.amount() : this.getAmount();
        this.memo = scheduleDto.memo() != null ? scheduleDto.memo() : this.getMemo();
        this.action = scheduleDto.action() != null ? scheduleDto.action() : this.action;
    }
}
