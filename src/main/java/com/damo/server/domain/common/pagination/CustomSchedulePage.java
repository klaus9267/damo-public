package com.damo.server.domain.common.pagination;

import com.damo.server.domain.schedule.ScheduleAmount;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import com.damo.server.domain.schedule.entity.ScheduleTransaction;
import lombok.Getter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class CustomSchedulePage extends PageImpl<ScheduleDto> {
    private final List<ScheduleAmount> amounts;

    public CustomSchedulePage(List<ScheduleDto> scheduleDtos, Pageable pageable, final List<ScheduleAmount> amounts, final LocalDateTime startedAt, final LocalDateTime endedAt) {
        super(scheduleDtos, pageable, scheduleDtos.size());
        if (startedAt != null) {
        amounts.addAll(ScheduleAmount.setAmounts(scheduleDtos, startedAt, endedAt));
        }
        this.amounts = amounts;
    }
}