package com.damo.server.domain.common.pagination.param;

import com.damo.server.domain.common.pagination.PaginationSortGroup;
import com.damo.server.domain.common.pagination.PaginationSortType;
import com.damo.server.domain.common.pagination.PaginationValidation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;


@Getter
public class SchedulePaginationParam extends AbstractPaginationParam {
    @Parameter(example = "0", required = true)
    private final Integer page;
    @Parameter(example = "10", required = true)
    private final Integer size;
    @Parameter(required = true, description = "조회 년,월 | 일은 아무 숫자나 상관 없음", example = "2024-01-01")
    private final LocalDate date;
    @Parameter(name = "direction", description = "default desc")
    private final Sort.Direction direction;

    @Parameter(description = "대상 정렬은 EVENT_DATE만 사용 가능")
    @PaginationValidation(sortGroup = PaginationSortGroup.SCHEDULE)
    private final PaginationSortType field;

    public SchedulePaginationParam(final Integer page, final Integer size, final LocalDate date, final PaginationSortType field, final Sort.Direction direction) {
        this.page = Math.max(page, 0);
        this.size = Math.max(size, 10);
        this.date = date;
        this.field = field == null ? PaginationSortType.EVENT_DATE : field;
        this.direction = direction == null ? Sort.Direction.DESC : direction;
    }

    public Pageable toPageable() {
        return PageRequest.of(page, size, field.toSort(direction));
    }
}
