package com.damo.server.domain.transaction.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionGift {
    CASH("CASH", "현금"),
    GIFT("GIFT", "선물"),
    MOBILE_VOUCHER("MOBILE_VOUCHER", "기프티콘"),
    ETC("ETC", "기타");

    private final String key;
    private final String title;
}
