package com.damo.server.domain.schedule;

public record ScheduleAmount(
        Long totalGivingAmount,
        Long totalReceivingAmount) {
    public ScheduleAmount(final Long totalGivingAmount, final Long totalReceivingAmount) {
        this.totalReceivingAmount = totalReceivingAmount;
        this.totalGivingAmount = totalGivingAmount;
    }
}
