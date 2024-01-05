package com.damo.server.domain.common.pagination.param;

import com.damo.server.domain.common.pagination.PaginationSortGroup;
import com.damo.server.domain.common.pagination.PaginationSortType;
import com.damo.server.domain.common.pagination.PaginationValidation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.beans.ConstructorProperties;

@Getter
public class PersonPaginationParam extends AbstractPaginationParam {
    @Parameter(name = "page", example = "0", required = true)
    private final Integer page;
    @Parameter(name = "size", example = "10", required = true)
    private final Integer size;
    @Parameter(name = "keyword", example = "키워드", description = "현재는 단어가 일치해야만 검색이 됨, 이름 및 연락처 검색 가능")
    private final String keyword;
    @Parameter(description = "(사용X) 기본적으로 NAME이 사전순으로 정렬됨", deprecated = true)
    private final Sort.Direction direction;

    // TODO: NAME, CREATED_AT, RELATION을 동적으로 선택할 수 있도록 변경
    @PaginationValidation(sortGroup = PaginationSortGroup.PERSON)
    @Parameter(description = "(사용X) 기본적으로 NAME이 사전순으로 정렬됨", deprecated = true)
    private final PaginationSortType field;

    @ConstructorProperties({"page", "size", "field", "direction", "keyword"})
    public PersonPaginationParam(final Integer page, final Integer size, final PaginationSortType field, final Sort.Direction direction, final String keyword) {
        this.page = Math.max(page, 0);
        this.size = Math.max(size, 10);
        this.field = field == null ? PaginationSortType.NAME : field;
        this.direction = direction == null ? Sort.Direction.ASC : direction;
        this.keyword = keyword == null ? null : wrapLikeQueryTo(keyword);
    }

    public Pageable toPageable() {
        return PageRequest.of(page, size, field.toSort(direction));
    }

    private String wrapLikeQueryTo(final String keyword) {
        return "%" + keyword + "%";
    }
}
