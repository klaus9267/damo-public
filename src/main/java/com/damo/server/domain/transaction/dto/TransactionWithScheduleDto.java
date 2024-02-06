package com.damo.server.domain.transaction.dto;

import com.damo.server.domain.person.dto.PersonDto;
import com.damo.server.domain.person.entity.Person;
import com.damo.server.domain.schedule.Schedule;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import com.damo.server.domain.transaction.entity.Transaction;
import com.damo.server.domain.transaction.entity.TransactionAmount;
import com.damo.server.domain.transaction.entity.TransactionCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

/**
 * {@code TransactionWithScheduleDto}는 내역, 일정 정보를 포함한 DTO 클래스입니다.
 */
@Getter
@AllArgsConstructor
@Builder
public class TransactionWithScheduleDto {
  private final Long id;
  private final PersonDto person;
  private final ScheduleDto schedule;
  private final TransactionAmount transactionAmount;
  private final TransactionCategory category;
  private final String memo;
  private final Timestamp createdAt;
  private final Timestamp updatedAt;
  
  public TransactionWithScheduleDto(final Transaction transaction, final Person person, final Schedule schedule) {
    this.id = transaction.getId();
    this.person = PersonDto.toPersonDto(person);
    this.schedule = ScheduleDto.from(schedule);
    this.transactionAmount = transaction.getTransactionAmount();
    this.category = transaction.getCategory();
    this.memo = transaction.getMemo();
    this.createdAt = transaction.getCreatedAt();
    this.updatedAt = transaction.getUpdatedAt();
  }
  
  private TransactionWithScheduleDto(final Transaction transaction) {
    this.id = transaction.getId();
    this.person = PersonDto.toPersonDto(transaction.getPerson());
    this.schedule = ScheduleDto.from(transaction.getSchedule());
    this.transactionAmount = transaction.getTransactionAmount();
    this.category = transaction.getCategory();
    this.memo = transaction.getMemo();
    this.createdAt = transaction.getCreatedAt();
    this.updatedAt = transaction.getUpdatedAt();
  }
  
  public static TransactionWithScheduleDto from(final Transaction transaction) {
    return new TransactionWithScheduleDto(transaction);
  }
}
