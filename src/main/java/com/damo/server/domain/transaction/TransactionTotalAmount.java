package com.damo.server.domain.transaction;

import lombok.Getter;

@Getter
public class TransactionTotalAmount {
    private final Long totalGivingAmount;
    private final Long totalReceivingAmount;

    public TransactionTotalAmount(final TransactionAmount totalGivingAmount, final TransactionAmount totalReceivingAmount) {
        this.totalReceivingAmount = totalReceivingAmount.getAmount() != null ? totalReceivingAmount.getAmount() : 0;
        this.totalGivingAmount = totalGivingAmount.getAmount() != null ? totalGivingAmount.getAmount() : 0;
    }
}
