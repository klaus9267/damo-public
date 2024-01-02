package com.damo.server.domain.common.pagination.param;

import com.damo.server.domain.common.pagination.PaginationSortGroup;
import com.damo.server.domain.common.pagination.PaginationSortType;
import com.damo.server.domain.common.pagination.PaginationValidation;
import com.damo.server.domain.transaction.entity.TransactionAction;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;


@Getter
public class TransactionPaginationParam extends AbstractPaginationParam {
    @Parameter(example = "0", required = true)
    private final Integer page;
    @Parameter(example = "10", required = true)
    private final Integer size;
    @Parameter(required = true)
    private final TransactionAction action;
    @Parameter(example = "2023-12-09T:00:00:00", required = false, hidden = true)
    private final LocalDateTime startedAt;
    @Parameter(example = "2023-12-10T:00:00:00", required = false, hidden = true)
    private final LocalDateTime endedAt;

    @Parameter(description = "대상 정렬은 TRANSACTION, EVENT_DATE만 사용 가능")
    @PaginationValidation(sortGroup = PaginationSortGroup.TRANSACTION)
    private final PaginationSortType field = PaginationSortType.EVENT_DATE;

    public TransactionPaginationParam(final Integer page, final Integer size, final TransactionAction action, final LocalDateTime startedAt, final LocalDateTime endedAt) {
        this.page = Math.max(page, 0);
        this.size = Math.max(size, 10);
        this.action = action;
        this.startedAt = startedAt == null ? LocalDateTime.now().minusMonths(1) : startedAt;
        this.endedAt = endedAt == null ? LocalDateTime.now() : endedAt;
    }

    public Pageable toPageable() {
        return PageRequest.of(page, size, field.toSort(direction));
    }
}
