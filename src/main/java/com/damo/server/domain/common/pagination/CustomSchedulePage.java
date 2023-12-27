package com.damo.server.domain.common.pagination;

import com.damo.server.domain.schedule.ScheduleAmount;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Getter
public class CustomSchedulePage extends PageImpl<ScheduleDto> {
    private final ScheduleAmount amounts;

    public CustomSchedulePage(Page<ScheduleDto> page) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
        this.amounts = null;
    }

    public CustomSchedulePage(final Page<ScheduleDto> page, final ScheduleAmount scheduleAmount) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
        this.amounts = scheduleAmount;
    }
}