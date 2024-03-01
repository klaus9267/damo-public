package com.damo.server.domain.transaction.dto;

import com.damo.server.domain.person.dto.PersonDto;
import com.damo.server.domain.person.entity.Person;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import com.damo.server.domain.schedule.entity.Schedule;
import com.damo.server.domain.transaction.entity.Transaction;
import com.damo.server.domain.transaction.entity.TransactionCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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
  private final TransactionAmountDto transactionAmount;
  private final TransactionCategory category;
  private final String memo;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
  
  /**
   * TransactionWithScheduleDto 객체를 생성하는 생성자.
   * Transaction, 그에 연결된 Person 정보, 그리고 연결된 Schedule 정보를 이용하여 TransactionWithScheduleDto를 생성한다.
   *
   * @param transaction Transaction 정보를 담고 있는 Transaction 객체
   * @param person      Transaction에 연결된 Person 객체
   * @param schedule    Transaction에 연결된 Schedule 객체
   */
  public TransactionWithScheduleDto(
      final Transaction transaction,
      final Person person,
      final Schedule schedule
  ) {
    this.id = transaction.getId();
    this.person = PersonDto.toPersonDto(person);
    this.schedule = ScheduleDto.from(schedule);
    this.transactionAmount = TransactionAmountDto.from(transaction.getTransactionAmount());
    this.category = transaction.getCategory();
    this.memo = transaction.getMemo();
    this.createdAt = transaction.getCreatedAt();
    this.updatedAt = transaction.getUpdatedAt();
  }
  
  private TransactionWithScheduleDto(final Transaction transaction) {
    this.id = transaction.getId();
    this.person = PersonDto.toPersonDto(transaction.getPerson());
    this.schedule = ScheduleDto.from(transaction.getSchedule());
    this.transactionAmount = TransactionAmountDto.from(transaction.getTransactionAmount());
    this.category = transaction.getCategory();
    this.memo = transaction.getMemo();
    this.createdAt = transaction.getCreatedAt();
    this.updatedAt = transaction.getUpdatedAt();
  }
  
  public static TransactionWithScheduleDto from(final Transaction transaction) {
    return new TransactionWithScheduleDto(transaction);
  }
}
