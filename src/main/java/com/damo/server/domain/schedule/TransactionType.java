package com.damo.server.domain.schedule;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionType {
    GIVING("GIVING", "준"),
    RECEIVING("RECEIVING", "받은");

    private final String key;
    private final String title;
}
