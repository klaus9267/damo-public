package com.damo.server.domain.schedule.dto;

import java.time.LocalDateTime;

public record RequestScheduleDto(
        Long personId,
        LocalDateTime date,
        Integer amount,
        String memo,
        String event,
        String status,
        String transaction
) {}