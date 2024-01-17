package com.damo.server.domain.schedule.dto;

import com.damo.server.domain.schedule.Schedule;
import com.damo.server.domain.schedule.ScheduleStatus;
import com.damo.server.domain.transaction.dto.TransactionDto;
import com.damo.server.domain.transaction.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class ScheduleDto {
    private final Long id;
    private final String event;
    private final LocalDateTime eventDate;
    private final String memo;
    private final ScheduleStatus status;
    private final TransactionDto transaction;
    private final Timestamp createdAt;
    private final Timestamp updatedAt;

    public ScheduleDto(final Schedule schedule, final Transaction transaction) {
        this.id = schedule.getId();
        this.event = schedule.getEvent();
        this.eventDate = schedule.getEventDate();
        this.memo = schedule.getMemo();
        this.status = schedule.getStatus();
        this.transaction = TransactionDto.from(transaction);
        this.createdAt = schedule.getCreatedAt();
        this.updatedAt = schedule.getUpdatedAt();
    }

    public static ScheduleDto from(final Schedule schedule) {
        TransactionDto transactionDto = schedule.getTransaction() == null ? null : TransactionDto.from(schedule.getTransaction());

        return ScheduleDto.builder()
                          .id(schedule.getId())
                          .event(schedule.getEvent())
                          .eventDate(schedule.getEventDate())
                          .memo(schedule.getMemo())
                          .status(schedule.getStatus())
                          .transaction(transactionDto)
                          .createdAt(schedule.getCreatedAt())
                          .updatedAt(schedule.getUpdatedAt())
                          .build();
    }
}
