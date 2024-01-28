package com.damo.server.domain.schedule.dto;

import com.damo.server.domain.schedule.Schedule;
import com.damo.server.domain.schedule.ScheduleStatus;
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
    private final Timestamp createdAt;
    private final Timestamp updatedAt;

    private ScheduleDto(final Schedule schedule) {
        this.id = schedule.getId();
        this.event = schedule.getEvent();
        this.eventDate = schedule.getEventDate();
        this.memo = schedule.getMemo();
        this.status = schedule.getStatus();
        this.createdAt = schedule.getCreatedAt();
        this.updatedAt = schedule.getUpdatedAt();
    }

    public static ScheduleDto from(final Schedule schedule) {
        return new ScheduleDto(schedule);
    }
}
