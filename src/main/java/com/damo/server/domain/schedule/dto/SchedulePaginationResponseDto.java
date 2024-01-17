package com.damo.server.domain.schedule.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class SchedulePaginationResponseDto {
    private final Integer totalPages;
    private final Long totalElements;
    private final List<ScheduleDto> schedules;

    private SchedulePaginationResponseDto(final Page<ScheduleDto> schedulePage) {
        this.totalPages = schedulePage.getTotalPages();
        this.totalElements = schedulePage.getTotalElements();
        this.schedules = schedulePage.getContent();
    }

    public static SchedulePaginationResponseDto from(final Page<ScheduleDto> schedulePage) {
        return new SchedulePaginationResponseDto(schedulePage);
    }
}
