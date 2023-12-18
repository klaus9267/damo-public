package com.damo.server.domain.schedule;

import com.damo.server.domain.schedule.dto.ScheduleDto;
import com.damo.server.domain.schedule.entity.ScheduleTransaction;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class ScheduleAmount {
    private final Long totalAmount;
    private final ScheduleTransaction transaction;
    private final LocalDateTime startedAt;
    private final LocalDateTime endedAt;

    public ScheduleAmount(final Long totalAmount, final ScheduleTransaction transaction, final LocalDateTime startedAt, final LocalDateTime endedAt) {
        this.totalAmount = totalAmount;
        this.transaction = transaction;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public ScheduleAmount(final Long totalAmount, final ScheduleTransaction transaction) {
        this.totalAmount = totalAmount;
        this.transaction = transaction;
        this.startedAt = null;
        this.endedAt = null;
    }

    public static List<ScheduleAmount> setAmounts(final List<ScheduleDto> scheduleDtos, final LocalDateTime startedAt, final LocalDateTime endedAt) {
        Map<ScheduleTransaction, Long> map = scheduleDtos.stream().collect(Collectors.groupingBy(ScheduleDto::getTransaction, Collectors.summingLong(ScheduleDto::getAmount)));
        ScheduleAmount termGiving = new ScheduleAmount(map.getOrDefault(ScheduleTransaction.GIVING, 0L), ScheduleTransaction.GIVING, startedAt, endedAt);
        ScheduleAmount termReceiving = new ScheduleAmount(map.getOrDefault(ScheduleTransaction.RECEIVING, 0L), ScheduleTransaction.RECEIVING, startedAt, endedAt);
        return List.of(termGiving, termReceiving);
    }
}
