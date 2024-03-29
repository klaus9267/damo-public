package com.damo.server.domain.schedule.entity;

import com.damo.server.domain.schedule.dto.RequestCreateScheduleDto;
import com.damo.server.domain.schedule.dto.RequestUpdateScheduleDto;
import com.damo.server.domain.transaction.dto.RequestCreateTransactionDto;
import com.damo.server.domain.transaction.entity.Transaction;
import com.damo.server.domain.user.entity.User;
import jakarta.persistence.Column;
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
 * 'Schedule' 클래스는 시스템에서 일정을 나타냅니다.
 * 이 클래스는 데이터베이스의 "schedules" 테이블과 매핑됩니다.
 */
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
  
  @Column(nullable = false, name = "event_date")
  private LocalDateTime eventDate;
  
  @Column(columnDefinition = "TEXT")
  private String memo;
  
  @Enumerated(EnumType.STRING)
  private ScheduleStatus status;
  
  @Column(name = "created_at")
  @CreationTimestamp
  private LocalDateTime createdAt;
  
  @Column(name = "updated_at")
  @UpdateTimestamp
  private LocalDateTime updatedAt;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;
  
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "transaction_id")
  private Transaction transaction;

  /**
   * Schedule 객체를 생성하는 생성자.
   * RequestCreateTransactionDto와 Transaction을 기반으로 Schedule을 생성한다.
   * @param transactionDto Schedule을 생성하는 데 필요한 트랜잭션 정보를 담고 있는 RequestCreateTransactionDto
   * @param transaction Schedule에 연결된 트랜잭션 객체
   */
  public Schedule(final RequestCreateTransactionDto transactionDto, final Transaction transaction) {
    this.event = transactionDto.event();
    this.eventDate = transactionDto.eventDate();
    this.transaction = transaction;
    this.user = transaction.getUser();
  }

  /**
   * Schedule 객체를 생성하는 생성자.
   * RequestCreateScheduleDto와 사용자 ID를 기반으로 Schedule을 생성한다.
   * @param scheduleDto Schedule을 생성하는 데 필요한 일정 정보를 담고 있는 RequestCreateScheduleDto
   * @param userId Schedule에 연결된 사용자의 ID
   */
  public Schedule(final RequestCreateScheduleDto scheduleDto, final Long userId) {
    this.event = scheduleDto.event();
    this.eventDate = scheduleDto.eventDate();
    this.status = scheduleDto.status();
    this.memo = scheduleDto.memo();
    this.user = User.builder().id(userId).build();
    this.transaction = scheduleDto.transactionId() == null
        ? null
        : Transaction.builder().id(scheduleDto.transactionId()).build();
  }
  
  /**
   * RequestCreateTransactionDto과 Transaction으로부터 Schedule 객체를 생성하는 정적 메서드.
   */
  public static Schedule from(
      final RequestCreateTransactionDto transactionDto,
      final Transaction transaction
  ) {
    return new Schedule(transactionDto, transaction);
  }
  
  /**
   * RequestCreateTransactionDto로부터 Schedule 객체를 생성하는 정적 메서드.
   */
  public static Schedule from(final RequestCreateScheduleDto scheduleDto, final Long userId) {
    return new Schedule(scheduleDto, userId);
  }
  
  /**
   * RequestUpdateScheduleDto로부터 일정 정보를 업데이트하는 메서드.
   */
  public void changeSchedule(final RequestUpdateScheduleDto scheduleDto) {
    this.event = scheduleDto.event();
    this.eventDate = scheduleDto.eventDate();
    this.status = scheduleDto.status();
    this.memo = scheduleDto.memo();
    
    if (scheduleDto.transactionId() != null) {
      this.transaction = Transaction.builder().id(scheduleDto.transactionId()).build();
    }
  }
}
