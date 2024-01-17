package com.damo.server.domain.common.pagination;

import com.damo.server.domain.transaction.TransactionTotalAmount;
import com.damo.server.domain.transaction.dto.TransactionWithScheduleDto;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Getter
public class CustomSchedulePage extends PageImpl<TransactionWithScheduleDto> {
    private final TransactionTotalAmount amounts;

    public CustomSchedulePage(final Page<TransactionWithScheduleDto> page) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
        this.amounts = null;
    }

    public CustomSchedulePage(final Page<TransactionWithScheduleDto> page, final TransactionTotalAmount transactionTotalAmount) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
        this.amounts = transactionTotalAmount;
    }
}