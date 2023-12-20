package com.damo.server.domain.schedule;

import com.damo.server.domain.schedule.dto.ScheduleDto;
import com.damo.server.domain.schedule.entity.ScheduleTransaction;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record ScheduleAmount(
        Long totalGiving,
        Long totalReceiving) {
    public ScheduleAmount(final Long totalGiving, final Long totalReceiving) {
        this.totalReceiving = totalReceiving;
        this.totalGiving = totalGiving;
    }

    public static ScheduleAmount setAmount(List<ScheduleDto> scheduleDtos) {
        Map<ScheduleTransaction, Long> map = scheduleDtos.stream().collect(Collectors.groupingBy(ScheduleDto::getTransaction, Collectors.summingLong(ScheduleDto::getAmount)));
        return new ScheduleAmount(map.getOrDefault(ScheduleTransaction.GIVING, 0L), map.getOrDefault(ScheduleTransaction.RECEIVING, 0L));
    }
}
