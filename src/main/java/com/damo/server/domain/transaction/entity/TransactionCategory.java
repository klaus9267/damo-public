package com.damo.server.domain.transaction.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionCategory {
    CASH("CASH", "현금"),
    GIFT("GIFT", "선물"),
    MOBILE_GIFTS("MOBILE_GIFTS", "기프티콘"),
    ETC("ETC", "기타");

    private final String key;
    private final String title;
}
