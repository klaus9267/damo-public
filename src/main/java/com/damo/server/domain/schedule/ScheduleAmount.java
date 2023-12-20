package com.damo.server.domain.schedule;

public record ScheduleAmount(
        Long totalGiving,
        Long totalReceiving) {
    public ScheduleAmount(final Long totalGiving, final Long totalReceiving) {
        this.totalReceiving = totalReceiving;
        this.totalGiving = totalGiving;
    }
}
