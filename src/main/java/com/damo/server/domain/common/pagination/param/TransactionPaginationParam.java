package com.damo.server.domain.common.pagination.param;

import com.damo.server.domain.common.pagination.PaginationSortGroup;
import com.damo.server.domain.common.pagination.PaginationSortType;
import com.damo.server.domain.common.pagination.PaginationValidation;
import com.damo.server.domain.transaction.entity.TransactionAction;
import io.swagger.v3.oas.annotations.Parameter;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * {@code TransactionPaginationParam}은 거래 목록 조회를 위한 페이지네이션 파라미터 클래스입니다.
 * AbstractPaginationParam을 상속하며, 거래 목록 조회에 필요한 검색 및 정렬 옵션을 제공합니다.
 */
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

  @Parameter(description = "대상 정렬은 ACTION, SCHEDULE_EVENT_DATE만 사용 가능 | null 입력시 SCHEDULE_EVENT_DATE로 설정")
  @PaginationValidation(sortGroup = PaginationSortGroup.TRANSACTION)
  private final PaginationSortType field;

  /**
   * TransactionPaginationParam의 생성자로, 필수 및 기본값이 지정된 파라미터들을 초기화합니다.
   *
   * @param page    페이지 번호
   * @param size    페이지 크기
   * @param action  거래 유형
   * @param startedAt 조회 시작 일시
   * @param endedAt   조회 종료 일시
   * @param field   정렬 기준 필드
   * @param direction 정렬 방향
   */
  public TransactionPaginationParam(
      final Integer page,
      final Integer size,
      final TransactionAction action,
      final LocalDateTime startedAt,
      final LocalDateTime endedAt,
      final PaginationSortType field, final Sort.Direction direction
  ) {
    this.page = Math.max(page, 0);
    this.size = Math.max(size, 10);
    this.action = action;
    this.startedAt = startedAt == null ? LocalDateTime.now().minusMonths(1) : startedAt;
    this.endedAt = endedAt == null ? LocalDateTime.now() : endedAt;
    this.field = field == null ? PaginationSortType.SCHEDULE_EVENT_DATE : field;
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
}
