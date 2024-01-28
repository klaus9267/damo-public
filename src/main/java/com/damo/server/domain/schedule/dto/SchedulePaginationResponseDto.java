package com.damo.server.domain.schedule.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class SchedulePaginationResponseDto {
    private final Integer totalPages;
    private final Long totalElements;
    private final List<ScheduleWithTransactionDto> schedules;

    private SchedulePaginationResponseDto(final Page<ScheduleWithTransactionDto> schedulePage) {
        this.totalPages = schedulePage.getTotalPages();
        this.totalElements = schedulePage.getTotalElements();
        this.schedules = schedulePage.getContent();
    }

    public static SchedulePaginationResponseDto from(final Page<ScheduleWithTransactionDto> schedulePage) {
        return new SchedulePaginationResponseDto(schedulePage);
    }
}
