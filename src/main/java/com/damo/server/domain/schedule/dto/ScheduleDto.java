package com.damo.server.domain.schedule.dto;

import com.damo.server.domain.person.dto.PersonDto;
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
    private PersonDto person;
    private LocalDateTime date;
    private Integer amount;
    private String memo;
    private String event;
    private String status;
    private String transaction;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
