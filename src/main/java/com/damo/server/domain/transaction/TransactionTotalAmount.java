package com.damo.server.domain.transaction;

import com.damo.server.domain.transaction.entity.TransactionAmount;
import lombok.Getter;

@Getter
public class TransactionTotalAmount {
    private final Long totalGivingAmount;
    private final Long totalReceivingAmount;

    public TransactionTotalAmount(final TransactionAmount totalGivingAmount, final TransactionAmount totalReceivingAmount) {
        this.totalReceivingAmount = totalReceivingAmount.getAmount();
        this.totalGivingAmount = totalGivingAmount.getAmount();
    }
}
