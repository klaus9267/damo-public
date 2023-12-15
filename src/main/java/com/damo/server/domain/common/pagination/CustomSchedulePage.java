package com.damo.server.domain.common.pagination;

import com.damo.server.domain.schedule.dto.ScheduleDto;
import lombok.Getter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
public class CustomSchedulePage extends PageImpl<ScheduleDto> {
    private final Integer totalGiving;
    private final Integer totalReceiving;

    public CustomSchedulePage(List<ScheduleDto> content, Pageable pageable, long total, Integer totalGiving, Integer totalReceiving) {
        super(content, pageable, total);
        this.totalGiving = totalGiving;
        this.totalReceiving = totalReceiving;
    }
}