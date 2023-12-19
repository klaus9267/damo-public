package com.damo.server.domain.common.pagination.param;

import com.damo.server.domain.common.pagination.PaginationSortGroup;
import com.damo.server.domain.common.pagination.PaginationSortType;
import com.damo.server.domain.common.pagination.PaginationValidation;
import com.damo.server.domain.schedule.entity.ScheduleTransaction;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;


@ParameterObject
@Getter
public class SchedulePaginationParam extends AbstractPaginationParam {
    private final Integer page;
    private final Integer size;
    private ScheduleTransaction transaction;
    private final LocalDateTime startedAt;
    private final LocalDateTime endedAt;

    @Schema(hidden = true)
    @PaginationValidation(sortGroup = PaginationSortGroup.SCHEDULE)
    private final PaginationSortType field = PaginationSortType.DATE;

    public SchedulePaginationParam(final Integer page, final Integer size, final ScheduleTransaction transaction, final LocalDateTime startedAt, final LocalDateTime endedAt) {
        this.page = Math.max(page, 0);
        this.size = Math.max(size, 10);
        this.transaction = transaction;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public Pageable toPageable() {
        return PageRequest.of(page, size, field.toSort(direction));
    }
}
