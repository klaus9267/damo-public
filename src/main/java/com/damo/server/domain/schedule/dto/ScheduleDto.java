package com.damo.server.domain.schedule.dto;

import com.damo.server.domain.schedule.Schedule;
import com.damo.server.domain.schedule.ScheduleStatus;
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
        Timestamp createdAt,
        Timestamp updatedAt
) {
    public static ScheduleDto from(Schedule schedule) {
        return ScheduleDto.builder()
                          .id(schedule.getId())
                          .title(schedule.getEvent())
                          .eventDate(schedule.getEventDate())
                          .memo(schedule.getMemo())
                          .status(schedule.getStatus())
                          .createdAt(schedule.getCreatedAt())
                          .updatedAt(schedule.getUpdatedAt())
                          .build();
    }
}
