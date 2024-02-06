package com.damo.server.domain.common.pagination.param;

import com.damo.server.domain.common.pagination.PaginationSortGroup;
import com.damo.server.domain.common.pagination.PaginationSortType;
import com.damo.server.domain.common.pagination.PaginationValidation;
import io.swagger.v3.oas.annotations.Parameter;
import java.time.LocalDate;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * {@code SchedulePaginationParam}은 행사 목록 조회를 위한 페이지네이션 파라미터 클래스입니다.
 * AbstractPaginationParam을 상속하며, 행사 목록 조회에 필요한 검색 및 정렬 옵션을 제공합니다.
 */
@Getter
public class SchedulePaginationParam extends AbstractPaginationParam {
  @Parameter(example = "0", required = true)
  private final Integer page;
  @Parameter(example = "10", required = true)
  private final Integer size;
  @Parameter(description = "조회 년,월 | 일은 아무 숫자나 상관 없음", example = "2024-01-01")
  private final LocalDate date;
  @Parameter(hidden = true)
  private final Integer year;
  @Parameter(hidden = true)
  private final Integer month;
  @Parameter(name = "keyword", example = "키워드", description = "현재는 단어가 일치해야만 검색이 됨, 행사 검색 가능")
  private final String keyword;
  @Parameter(name = "direction", description = "default desc")
  private final Sort.Direction direction;

  @Parameter(description = "대상 정렬은 EVENT_DATE만 사용 가능")
  @PaginationValidation(sortGroup = PaginationSortGroup.SCHEDULE)
  private final PaginationSortType field;

  /**
   * SchedulePaginationParam의 생성자로, 필수 및 기본값이 지정된 파라미터들을 초기화합니다.
   *
   * @param page    페이지 번호
   * @param size    페이지 크기
   * @param date    조회 날짜
   * @param keyword   검색 키워드
   * @param field   정렬 기준 필드
   * @param direction 정렬 방향
   */
  public SchedulePaginationParam(
      final Integer page,
      final Integer size,
      final LocalDate date,
      final String keyword,
      final PaginationSortType field,
      final Sort.Direction direction
  ) {
    this.page = Math.max(page, 0);
    this.size = Math.max(size, 10);
    this.date = date;
    this.year = date == null ? null : date.getYear();
    this.month = date == null ? null : date.getMonthValue();
    this.keyword = keyword == null ? null : wrapLikeQueryTo(keyword);
    this.field = field == null ? PaginationSortType.EVENT_DATE : field;
    this.direction = direction == null ? Sort.Direction.DESC : direction;
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
