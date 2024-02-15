package com.damo.server.domain.schedule.dto;

import com.damo.server.domain.schedule.entity.Schedule;
import com.damo.server.domain.schedule.entity.ScheduleStatus;
import com.damo.server.domain.transaction.dto.TransactionDto;
import com.damo.server.domain.transaction.entity.Transaction;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * {@code ScheduleWithTransactionDto}는 내역을 포함한 일정의 DTO 클래스입니다.
 */
@Builder
@AllArgsConstructor
@Getter
public class ScheduleWithTransactionDto {
  private final Long id;
  private final String event;
  private final LocalDateTime eventDate;
  private final String memo;
  private final ScheduleStatus status;
  private final TransactionDto transaction;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
  
  /**
   * 주어진 일정과 내역으로 {@code ScheduleWithTransactionDto}를 생성합니다.
   */
  public ScheduleWithTransactionDto(final Schedule schedule, final Transaction transaction) {
    this.id = schedule.getId();
    this.event = schedule.getEvent();
    this.eventDate = schedule.getEventDate();
    this.memo = schedule.getMemo();
    this.status = schedule.getStatus();
    this.transaction = schedule.getTransaction() == null ? null : TransactionDto.from(transaction);
    this.createdAt = schedule.getCreatedAt();
    this.updatedAt = schedule.getUpdatedAt();
  }
  
  /**
   * 주어진 {@code Schedule} 객체를 {@code ScheduleWithTransactionDto}로 변환합니다.
   *
   * @param schedule 변환할 대상 객체
   * @return 변환된 {@code ScheduleWithTransactionDto}
   */
  public static ScheduleWithTransactionDto from(final Schedule schedule) {
    return new ScheduleWithTransactionDto(schedule, schedule.getTransaction());
  }
}
