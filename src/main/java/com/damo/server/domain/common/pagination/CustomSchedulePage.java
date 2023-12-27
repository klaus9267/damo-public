package com.damo.server.domain.common.pagination;

import com.damo.server.domain.transaction.ScheduleAmount;
import com.damo.server.domain.transaction.dto.TransactionDto;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Getter
public class CustomSchedulePage extends PageImpl<TransactionDto> {
    private final ScheduleAmount amounts;

    public CustomSchedulePage(Page<TransactionDto> page) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
        this.amounts = null;
    }

    public CustomSchedulePage(final Page<TransactionDto> page, final ScheduleAmount scheduleAmount) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
        this.amounts = scheduleAmount;
    }
}