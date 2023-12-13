package com.damo.server.domain.schedule.dto;

public record ScheduleWithTotalAmountDto(
        ScheduleWithPersonDto scheduleWithPersonDto,
        Integer totalAmount) {}
