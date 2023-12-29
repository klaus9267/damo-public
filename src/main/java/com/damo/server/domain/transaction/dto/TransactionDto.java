package com.damo.server.domain.transaction.dto;

import com.damo.server.domain.person.dto.PersonDto;
import com.damo.server.domain.person.entity.Person;
import com.damo.server.domain.transaction.entity.Transaction;
import com.damo.server.domain.transaction.entity.TransactionAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class TransactionDto {
    private final Long id;
    private final PersonDto person;
    private final LocalDateTime eventDate;
    private final TransactionAmount amount;
    private final String memo;
    private final Timestamp createdAt;
    private final Timestamp updatedAt;

    public TransactionDto(final Transaction transaction, final Person person) {
        this.id = transaction.getId();
        this.person = PersonDto.toPersonDto(person);
        this.eventDate = transaction.getEventDate();
        this.amount = transaction.getAmount();
        this.memo = transaction.getMemo();
        this.createdAt = transaction.getCreatedAt();
        this.updatedAt = transaction.getUpdatedAt();
    }

    public static TransactionDto from(Transaction transaction) {
        return new TransactionDto(transaction, transaction.getPerson());
    }
}
