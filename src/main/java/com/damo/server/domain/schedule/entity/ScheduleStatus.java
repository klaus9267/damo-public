package com.damo.server.domain.schedule.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScheduleStatus {
    IMPORTANT("IMPORTANT", "중요"),
    NORMAL("NORMAL", "일반");

    private final String key;
    private final String title;
}
