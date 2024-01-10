package com.damo.server.domain.schedule.dto;

import com.damo.server.domain.schedule.Schedule;
import com.damo.server.domain.schedule.ScheduleStatus;
import com.damo.server.domain.transaction.dto.TransactionDto;
import lombok.Builder;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Builder
public record ScheduleDto(
        Long id,
        String title,
        LocalDateTime eventDate,
        String memo,
        ScheduleStatus status,
        TransactionDto transaction,
        Timestamp createdAt,
        Timestamp updatedAt
) {
    public static ScheduleDto from(final Schedule schedule) {
        TransactionDto transactionDto = schedule.getTransaction() == null ? null : TransactionDto.from(schedule.getTransaction());

        return ScheduleDto.builder()
                          .id(schedule.getId())
                          .title(schedule.getEvent())
                          .eventDate(schedule.getEventDate())
                          .memo(schedule.getMemo())
                          .status(schedule.getStatus())
                          .transaction(transactionDto)
                          .createdAt(schedule.getCreatedAt())
                          .updatedAt(schedule.getUpdatedAt())
                          .build();
    }
}
