package com.damo.server.domain.transaction.dto;

import com.damo.server.domain.person.dto.PersonDto;
import com.damo.server.domain.person.entity.Person;
import com.damo.server.domain.transaction.entity.Transaction;
import com.damo.server.domain.transaction.entity.TransactionAmount;
import com.damo.server.domain.transaction.entity.TransactionCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * {@code TransactionDto}는 내역 정보를 포함한 DTO 클래스입니다.
 */
@Getter
@AllArgsConstructor
@Builder
public class TransactionDto {
  private final Long id;
  private final PersonDto person;
  private final TransactionAmount transactionAmount;
  private final TransactionCategory category;
  private final String memo;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
  
  public TransactionDto(final Transaction transaction, final Person person) {
    this.id = transaction.getId();
    this.person = PersonDto.toPersonDto(person);
    this.transactionAmount = transaction.getTransactionAmount();
    this.category = transaction.getCategory();
    this.memo = transaction.getMemo();
    this.createdAt = transaction.getCreatedAt();
    this.updatedAt = transaction.getUpdatedAt();
  }
  
  public static TransactionDto from(final Transaction transaction) {
    return new TransactionDto(transaction, transaction.getPerson());
  }
}
