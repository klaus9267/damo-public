package com.damo.server.domain.person.dto;

import com.damo.server.domain.person.entity.Person;
import com.damo.server.domain.person.entity.PersonRelation;
import java.time.LocalDateTime;
import lombok.Getter;

/**
 * {@code PeopleWithTransactionCountDto}는 거래 건수를 포함한 대상의 DTO입니다.
 */
@Getter
public class PeopleWithTransactionCountDto {
  private final Long id;
  private final String name;
  private final String contact;
  private final PersonRelation relation;
  private final String memo;
  private final LocalDateTime createdAt;
  private final Long transactionCount;

  /**
   * 주어진 대상과 거래 건수로 {@code PeopleWithTransactionCountDto}를 생성합니다.
   */
  public PeopleWithTransactionCountDto(final Person person, final Long transactionCount) {
    this.id = person.getId();
    this.name = person.getName();
    this.contact = person.getContact();
    this.relation = person.getRelation();
    this.memo = person.getMemo();
    this.createdAt = person.getCreatedAt();
    this.transactionCount = transactionCount;
  }
}