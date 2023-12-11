package com.damo.server.domain.schedule.dto;

import java.time.LocalDate;

public record RequestScheduleDto(
        Long personId,
        LocalDate date,
        Integer amount,
        String memo,
        String event,
        String status,
        String transaction
) {}