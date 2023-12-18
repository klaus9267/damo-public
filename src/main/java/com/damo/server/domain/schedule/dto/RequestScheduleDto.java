package com.damo.server.domain.schedule.dto;

import com.damo.server.domain.schedule.entity.ScheduleStatus;
import com.damo.server.domain.schedule.entity.ScheduleTransaction;

import java.time.LocalDateTime;

public record RequestScheduleDto(
        Long personId,
        LocalDateTime date,
        Integer amount,
        String memo,
        String event,
        ScheduleStatus status,
        ScheduleTransaction transaction
) {}