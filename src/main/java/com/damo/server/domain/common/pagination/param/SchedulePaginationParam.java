package com.damo.server.domain.common.pagination.param;

import com.damo.server.domain.common.pagination.PaginationSortGroup;
import com.damo.server.domain.common.pagination.PaginationSortType;
import com.damo.server.domain.common.pagination.PaginationValidation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class SchedulePaginationParam extends AbstractPaginationParam {
    private final Integer page;
    private final Integer size;
    @PaginationValidation(sortGroup = PaginationSortGroup.SCHEDULE)
    private final PaginationSortType field = PaginationSortType.DATE;

    public SchedulePaginationParam(final Integer page, final Integer size) {
        this.page = Math.max(page, 0);
        this.size = Math.max(size, 10);
    }

    public Pageable toPageable() {
        return PageRequest.of(page, size, field.toSort(direction));
    }
}
