package com.damo.server.domain.common.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Sort;

@AllArgsConstructor
@Getter
public enum PaginationSortType {
    NAME("name"),
    RELATION("relation"),
    CREATED_AT("createdAt"),
    ACTION("transactionAmount.action"),
    EVENT_DATE("eventDate"),
    EMPTY(null);

    private final String field;

    public Sort toSort(Sort.Direction direction) {
        return Sort.by(direction, field);
    }
}
