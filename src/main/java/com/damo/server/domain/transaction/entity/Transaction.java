package com.damo.server.domain.transaction.entity;

import com.damo.server.domain.person.entity.Person;
import com.damo.server.domain.schedule.entity.Schedule;
import com.damo.server.domain.transaction.dto.RequestCreateTransactionDto;
import com.damo.server.domain.transaction.dto.RequestUpdateTransactionDto;
import com.damo.server.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


/**
 * `Transaction` 클래스는 시스템에서 내역을 나타냅니다.
 * 이 클래스는 데이터베이스의 "transactions" 테이블과 매핑됩니다.
 */
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
  
  /**
   * 거래 금액과 관련된 개체들을 분리한 클래스입니다.
   */
  @Embedded
  private TransactionAmount transactionAmount;
  
  @Column(columnDefinition = "TEXT")
  private String memo;
  
  @Column(name = "created_at")
  @CreationTimestamp
  private LocalDateTime createdAt;
  
  @Column(name = "updated_at")
  @UpdateTimestamp
  private LocalDateTime updatedAt;
  
  @Enumerated(EnumType.STRING)
  private TransactionCategory category;
  
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
    this.category = transactionDto.category();
    this.user = User.builder().id(userId).build();
    this.schedule = Schedule.from(transactionDto, this);
  }
  
  /**
   * RequestCreateTransactionDto로부터 Transaction 객체를 생성하는 정적 메서드.
   */
  public static Transaction from(
      final RequestCreateTransactionDto transactionDto,
      final Long userId
  ) {
    return new Transaction(transactionDto, userId);
  }
  
  /**
   * RequestUpdateTransactionDto로부터 내역 정보를 업데이트하는 메서드.
   */
  public void changeInfo(final RequestUpdateTransactionDto transactionDto) {
    this.transactionAmount = transactionDto.transactionAmount() != null
        ? transactionDto.transactionAmount()
        : this.getTransactionAmount();
    this.memo = transactionDto.memo() != null ? transactionDto.memo() : this.getMemo();
  }
}
