package com.damo.server.domain.common.pagination.param;

import com.damo.server.domain.common.pagination.PaginationSortType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public abstract class AbstractPaginationParam {
    protected int size = 10;
    protected int page = 0;
    private PaginationSortType field = PaginationSortType.EMPTY;
    protected Sort.Direction direction = Sort.Direction.DESC;

    public abstract Pageable toPageable();
}
