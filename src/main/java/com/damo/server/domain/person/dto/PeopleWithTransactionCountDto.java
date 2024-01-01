package com.damo.server.domain.person.dto;

import com.damo.server.domain.person.entity.Person;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class PeopleWithTransactionCountDto {
    private final Long id;
    private final String name;
    private final String contact;
    private final String relation;
    private final String memo;
    private final Timestamp createdAt;
    private final Long transactionCount;

    public PeopleWithTransactionCountDto(Person person, Long transactionCount) {
        this.id = person.getId();
        this.name = person.getName();
        this.contact = person.getContact();
        this.relation = person.getRelation();
        this.memo = person.getMemo();
        this.createdAt = person.getCreatedAt();
        this.transactionCount = transactionCount;
    }
}