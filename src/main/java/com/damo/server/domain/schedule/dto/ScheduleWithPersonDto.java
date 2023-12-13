package com.damo.server.domain.schedule.dto;

import com.damo.server.domain.person.Person;
import com.damo.server.domain.person.dto.PersonDto;
import com.damo.server.domain.schedule.entity.Schedule;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
public class ScheduleWithPersonDto {
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

    public ScheduleWithPersonDto(final Schedule schedule, final Person person) {
        this.id = schedule.getId();
        this.person = PersonDto.toPersonDto(person);
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