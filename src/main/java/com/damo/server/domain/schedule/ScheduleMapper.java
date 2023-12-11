package com.damo.server.domain.schedule;

import com.damo.server.domain.person.Person;
import com.damo.server.domain.schedule.dto.RequestScheduleDto;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import com.damo.server.domain.schedule.entity.Schedule;
import com.damo.server.domain.schedule.entity.ScheduleStatus;
import com.damo.server.domain.schedule.entity.ScheduleTransaction;

public class ScheduleMapper {
    public static ScheduleDto toDto(final Schedule schedule) {
        return null;
    }

    public static Schedule toEntity(final RequestScheduleDto scheduleDto) {
        return Schedule.builder()
                .person(Person.builder().id(scheduleDto.personId()).build())
                       .date(scheduleDto.date())
                       .amount(scheduleDto.amount())
                       .memo(scheduleDto.memo())
                       .event(scheduleDto.event())
                       .status(ScheduleStatus.valueOf(scheduleDto.status()))
                       .transaction(ScheduleTransaction.valueOf(scheduleDto.transaction()))
                       .build();
    }
}
