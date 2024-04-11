package com.damo.server.domain.common.pagination;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code PaginationValidation}은 페이지네이션 정보의 유효성을 검사하는 데 사용되는 커스텀 어노테이션입니다.
 * 메소드, 필드, 매개변수에 적용할 수 있습니다.
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { PaginationValidator.class })
public @interface PaginationValidation {
  /**
   * 유효성 검사 실패 시 출력할 메시지를 지정합니다.
   */
  String message() default "올바르지 않은 페이지 정보입니다.";

  /**
   * 페이지네이션 정렬 그룹을 설정합니다.
   */
  PaginationSortGroup sortGroup() default PaginationSortGroup.EMPTY;

  /**
   * 기본 그룹을 설정합니다.
   */
  Class<?>[] groups() default {};

  /**
   * 페이로드 정보를 설정합니다.
   */
  Class<? extends Payload>[] payload() default {};
}
