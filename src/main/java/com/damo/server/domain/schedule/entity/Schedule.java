package com.damo.server.domain.schedule.entity;

import com.damo.server.domain.schedule.dto.RequestCreateScheduleDto;
import com.damo.server.domain.schedule.dto.RequestUpdateScheduleDto;
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
    this.user = transaction.getUser();
  }
  
  public Schedule(final RequestCreateScheduleDto scheduleDto, final Long userId) {
    this.event = scheduleDto.event();
    this.eventDate = scheduleDto.eventDate();
    this.status = scheduleDto.status();
    this.memo = scheduleDto.memo();
    this.user = User.builder().id(userId).build();
    this.transaction = scheduleDto.transactionId() == null ? null : Transaction.builder().id(scheduleDto.transactionId()).build();
  }
  
  /**
   * RequestCreateTransactionDto과 Transaction으로부터 Schedule 객체를 생성하는 정적 메서드.
   */
  public static Schedule from(final RequestCreateTransactionDto transactionDto, final Transaction transaction) {
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
