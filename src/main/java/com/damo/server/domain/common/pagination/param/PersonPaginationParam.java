package com.damo.server.domain.common.pagination.param;

import com.damo.server.domain.common.pagination.PaginationSortGroup;
import com.damo.server.domain.common.pagination.PaginationSortType;
import com.damo.server.domain.common.pagination.PaginationValidation;
import io.swagger.v3.oas.annotations.Parameter;
import java.beans.ConstructorProperties;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * {@code PersonPaginationParam}은 대상 목록 조회를 위한 페이지네이션 파라미터 클래스입니다.
 * AbstractPaginationParam을 상속하며, 대상 목록 조회에 필요한 검색 및 정렬 옵션을 제공합니다.
 */
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

  /**
   * PersonPaginationParam의 생성자로, 필수 및 기본값이 지정된 파라미터들을 초기화합니다.
   *
   * @param page    페이지 번호
   * @param size    페이지 크기
   * @param field   정렬 기준 필드 (deprecated)
   * @param direction 정렬 방향 (deprecated)
   * @param keyword   검색 키워드
   */
  @ConstructorProperties({"page", "size", "field", "direction", "keyword"})
  public PersonPaginationParam(
      final Integer page,
      final Integer size,
      final PaginationSortType field,
      final Sort.Direction direction,
      final String keyword
  ) {
    this.page = Math.max(page, 0);
    this.size = Math.max(size, 10);
    this.field = field == null ? PaginationSortType.NAME : field;
    this.direction = direction == null ? Sort.Direction.ASC : direction;
    this.keyword = keyword == null ? null : wrapLikeQueryTo(keyword);
  }

  /**
   * 페이지네이션 파라미터를 Spring Data의 Pageable 객체로 변환합니다.
   *
   * @return Spring Data의 Pageable 객체
   */
  public Pageable toPageable() {
    return PageRequest.of(page, size, field.toSort(direction));
  }

  /**
   * 검색 키워드를 부분 일치하는 형태로 감싸주는 메서드입니다.
   *
   * @param keyword 검색 키워드
   * @return 부분 일치하는 형태로 감싸진 검색 키워드
   */
  private String wrapLikeQueryTo(final String keyword) {
    return "%" + keyword + "%";
  }
}
