package com.damo.server.domain.common.pagination.param;

import com.damo.server.domain.common.pagination.PaginationSortGroup;
import com.damo.server.domain.common.pagination.PaginationSortType;
import com.damo.server.domain.common.pagination.PaginationValidation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
public class PersonPaginationParam extends AbstractPaginationParam {
    @Parameter(name = "page", example = "0", required = true)
    private final Integer page;
    @Parameter(name = "size", example = "10", required = true)
    private final Integer size;
    @Parameter(name = "relation", example = "가족", description = "null 허용")
    private final String relation;
    @Parameter(name = "direction", description = "default desc")
    private final Sort.Direction direction;

    // TODO: NAME, CREATED_AT, RELATION을 동적으로 선택할 수 있도록 변경
    @PaginationValidation(sortGroup = PaginationSortGroup.PERSON)
    @Parameter(description = "대상 정렬은 NAME, CREATED_AT, RELATION 만 사용 가능")
    private final PaginationSortType field;

    public PersonPaginationParam(final Integer page, final Integer size, final String relation, final PaginationSortType field, final Sort.Direction direction) {
        this.page = Math.max(page, 0);
        this.size = Math.max(size, 10);
        this.relation = relation;
        this.field = field == null ? PaginationSortType.CREATED_AT : field;
        this.direction = direction == null ? Sort.Direction.DESC : direction;
    }

    public Pageable toPageable() {
        return PageRequest.of(page, size, field.toSort(direction));
    }
}
