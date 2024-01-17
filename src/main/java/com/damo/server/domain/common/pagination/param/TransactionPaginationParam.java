package com.damo.server.domain.common.pagination.param;

import com.damo.server.domain.common.pagination.PaginationSortGroup;
import com.damo.server.domain.common.pagination.PaginationSortType;
import com.damo.server.domain.common.pagination.PaginationValidation;
import com.damo.server.domain.transaction.entity.TransactionAction;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
    @Parameter(name = "direction", description = "default desc")
    private final Sort.Direction direction;

    @Parameter(description = "대상 정렬은 ACTION만 사용 가능")
    @PaginationValidation(sortGroup = PaginationSortGroup.TRANSACTION)
    private final PaginationSortType field;

    public TransactionPaginationParam(final Integer page, final Integer size, final TransactionAction action, final LocalDateTime startedAt, final LocalDateTime endedAt,
            final PaginationSortType field, final Sort.Direction direction) {
        this.page = Math.max(page, 0);
        this.size = Math.max(size, 10);
        this.action = action;
        this.startedAt = startedAt == null ? LocalDateTime.now().minusMonths(1) : startedAt;
        this.endedAt = endedAt == null ? LocalDateTime.now() : endedAt;
        this.field = field == null ? PaginationSortType.EVENT_DATE : field;
        this.direction = direction == null ? Sort.Direction.DESC : direction;
    }

    public Pageable toPageable() {
        return PageRequest.of(page, size, field.toSort(direction));
    }
}
