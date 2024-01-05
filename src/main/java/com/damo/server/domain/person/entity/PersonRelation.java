package com.damo.server.domain.person.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PersonRelation {
    FAMILY("FAMILY", "가족"),
    RELATIVE("RELATIVE", "친척"),
    FRIEND("FRIEND", "친구"),
    ACQUAINTANCE("ACQUAINTANCE", "지인"),
    COMPANY("COMPANY", "회사"),
    ETC("ETC", "기타");

    private final String key;
    private final String title;
}
