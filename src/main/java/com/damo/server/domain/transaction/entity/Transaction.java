package com.damo.server.domain.transaction.entity;

import com.damo.server.domain.person.entity.Person;
import com.damo.server.domain.schedule.Schedule;
import com.damo.server.domain.transaction.dto.RequestCreateTransactionDto;
import com.damo.server.domain.transaction.dto.RequestUpdateTransactionDto;
import com.damo.server.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

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

    @Embedded
    private TransactionAmount transactionAmount;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Enumerated(EnumType.STRING)
    @Schema(description = "거래 종류", example = "GIVING")
    private TransactionGift gift;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL)
    private Schedule schedule;

    private Transaction(final RequestCreateTransactionDto transactionDto, final Long userId) {
        this.person = Person.builder().id(transactionDto.personId()).build();
        this.transactionAmount = transactionDto.transactionAmount();
        this.memo = transactionDto.memo();
        this.gift = transactionDto.gift();
        this.user = User.builder().id(userId).build();
        this.schedule = Schedule.from(transactionDto, this);
    }

    public static Transaction from(final RequestCreateTransactionDto transactionDto, final Long userId) {
        return new Transaction(transactionDto, userId);
    }

    public void changeInfo(final RequestUpdateTransactionDto transactionDto) {
        this.transactionAmount = transactionDto.transactionAmount() != null ? transactionDto.transactionAmount() : this.getTransactionAmount();
        this.memo = transactionDto.memo() != null ? transactionDto.memo() : this.getMemo();
    }
}
