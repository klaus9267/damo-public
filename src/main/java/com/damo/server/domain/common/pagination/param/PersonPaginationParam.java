package com.damo.server.domain.common.pagination.param;

import com.damo.server.domain.common.pagination.PaginationSortGroup;
import com.damo.server.domain.common.pagination.PaginationSortType;
import com.damo.server.domain.common.pagination.PaginationValidation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PersonPaginationParam extends AbstractPaginationParam {
    private final int page;
    private final int size;
    public PersonPaginationParam(int page, int size) {
        this.page = Math.max(page, 0);
        this.size = Math.max(size, 10);
    }

    @PaginationValidation(sortGroup = PaginationSortGroup.PERSON)
    private final PaginationSortType field = PaginationSortType.CREATED_AT;

    public Pageable toPageable() {
        return PageRequest.of(page, size, field.toSort(direction));
    }
}
