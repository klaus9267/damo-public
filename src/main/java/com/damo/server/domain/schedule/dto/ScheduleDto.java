package com.damo.server.domain.schedule.dto;

import com.damo.server.domain.person.Person;
import com.damo.server.domain.person.dto.PersonDto;
import com.damo.server.domain.schedule.entity.Schedule;
import com.damo.server.domain.schedule.entity.ScheduleStatus;
import com.damo.server.domain.schedule.entity.ScheduleTransaction;
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
    private final ScheduleStatus status;
    private final ScheduleTransaction transaction;
    private final Timestamp createdAt;
    private final Timestamp updatedAt;

    public ScheduleDto(final Schedule schedule, final Person person) {
        this.id = schedule.getId();
        this.person = PersonDto.toPersonDto(person);
        this.date = schedule.getDate();
        this.amount = schedule.getAmount();
        this.memo = schedule.getMemo();
        this.event = schedule.getEvent();
        this.status = schedule.getStatus();
        this.transaction = schedule.getTransaction();
        this.createdAt = schedule.getCreatedAt();
        this.updatedAt = schedule.getUpdatedAt();
    }

    public ScheduleDto(final Schedule schedule) {
        this.id = schedule.getId();
        this.person = null;
        this.date = schedule.getDate();
        this.amount = schedule.getAmount();
        this.memo = schedule.getMemo();
        this.event = schedule.getEvent();
        this.status = schedule.getStatus();
        this.transaction = schedule.getTransaction();
        this.createdAt = schedule.getCreatedAt();
        this.updatedAt = schedule.getUpdatedAt();
    }
}
