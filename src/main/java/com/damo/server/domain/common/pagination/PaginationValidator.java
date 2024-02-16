package com.damo.server.domain.common.pagination;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * {@code PaginationValidator}는 페이지네이션 정보의 정렬 유효성을 검사하는 커스텀 유효성 검사기입니다.
 */
public class PaginationValidator implements ConstraintValidator<PaginationValidation, PaginationSortType> {
  private PaginationSortGroup pageSortGroup;

  /**
   * 초기화 메서드로 어노테이션에서 설정된 페이지네이션 정렬 그룹을 가져옵니다.
   */
  @Override
  public void initialize(PaginationValidation constraintAnnotation) {
    pageSortGroup = constraintAnnotation.sortGroup();
  }

  /**
   * 주어진 페이지네이션 정렬이 유효한지 여부를 판단합니다.
   */
  @Override
  public boolean isValid(PaginationSortType value, ConstraintValidatorContext context) {
    return pageSortGroup.getSortTypes().contains(value);
  }
}
