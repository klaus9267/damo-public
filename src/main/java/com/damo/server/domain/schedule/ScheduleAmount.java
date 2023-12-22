package com.damo.server.domain.schedule;

public record ScheduleAmount(
        Long totalGivingAmount,
        Long totalReceivingAmount) {
    public ScheduleAmount(final Long totalGivingAmount, final Long totalReceivingAmount) {
        this.totalReceivingAmount = totalReceivingAmount != null ? totalReceivingAmount : 0;
        this.totalGivingAmount = totalGivingAmount != null ? totalGivingAmount : 0;
    }
}
