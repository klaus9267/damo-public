package com.damo.server.domain.transaction.dto;

import com.damo.server.domain.person.dto.PersonDto;
import com.damo.server.domain.person.entity.Person;
import com.damo.server.domain.schedule.Schedule;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import com.damo.server.domain.transaction.entity.Transaction;
import com.damo.server.domain.transaction.entity.TransactionAmount;
import com.damo.server.domain.transaction.entity.TransactionGift;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@Builder
public class TransactionDto {
    private final Long id;
    private final PersonDto person;
    private final ScheduleDto schedule;
    private final TransactionAmount transactionAmount;
    private final TransactionGift gift;
    private final String memo;
    private final Timestamp createdAt;
    private final Timestamp updatedAt;

    public TransactionDto(final Transaction transaction, final Person person, final Schedule schedule) {
        this.id = transaction.getId();
        this.person = PersonDto.toPersonDto(person);
        this.schedule = ScheduleDto.from(schedule);
        this.transactionAmount = transaction.getTransactionAmount();
        this.gift = transaction.getGift();
        this.memo = transaction.getMemo();
        this.createdAt = transaction.getCreatedAt();
        this.updatedAt = transaction.getUpdatedAt();
    }

    public static TransactionDto from(Transaction transaction) {
        return new TransactionDto(transaction, transaction.getPerson(), transaction.getSchedule());
    }
}
