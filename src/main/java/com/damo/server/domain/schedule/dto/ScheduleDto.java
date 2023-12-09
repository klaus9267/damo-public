package com.damo.server.domain.schedule.dto;

import com.damo.server.domain.person.dto.PersonDto;
import com.damo.server.domain.schedule.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ScheduleDto {
    private final Long id;
    private final PersonDto person;
    private final LocalDateTime date;
    private final Integer amount;
    private final String memo;
    private final String event;
    private final String status;
    private final String transaction;
    private final Timestamp createdAt;
    private final Timestamp updatedAt;

    public ScheduleDto(final Schedule schedule) {
        this.id = schedule.getId();
        this.person = PersonDto.toPersonDto(schedule.getPerson());
        this.date = schedule.getDate();
        this.amount = schedule.getAmount();
        this.memo = schedule.getMemo();
        this.event = schedule.getEvent();
        this.status = schedule.getStatus().getTitle();
        this.transaction = schedule.getTransaction().getTitle();
        this.createdAt = schedule.getCreatedAt();
        this.updatedAt = schedule.getUpdatedAt();
    }
}
