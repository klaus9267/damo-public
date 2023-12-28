package com.damo.server.domain.transaction;

public record TransactionAmount(
        Long totalGivingAmount,
        Long totalReceivingAmount) {
    public TransactionAmount(final Long totalGivingAmount, final Long totalReceivingAmount) {
        this.totalReceivingAmount = totalReceivingAmount != null ? totalReceivingAmount : 0;
        this.totalGivingAmount = totalGivingAmount != null ? totalGivingAmount : 0;
    }
}
