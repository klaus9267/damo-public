package com.damo.server.domain.common.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public enum PaginationSortGroup {
    PERSON(List.of(PaginationSortType.NAME, PaginationSortType.RELATION, PaginationSortType.CREATED_AT)),
    SCHEDULE(List.of(PaginationSortType.TRANSACTION, PaginationSortType.DATE)),
    EMPTY(List.of(PaginationSortType.EMPTY));

    private final List<PaginationSortType> sortTypes;
}
