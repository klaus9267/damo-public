package com.damo.server.domain.transaction.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionAction {
    TOTAL("TOTAL","전체"),
    GIVING("GIVING", "준"),
    RECEIVING("RECEIVING", "받은");

    private final String key;
    private final String title;
}
