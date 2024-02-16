package com.damo.server.application.controller.validation.transaction;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code ActionValid} 어노테이션은 {@link ActionValidator}에서 제공하는 유효성 검사를 지정하는 데 사용됩니다.
 * 이 어노테이션은 필드에 적용할 수 있으며 런타임에 유지됩니다.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ActionValidator.class)
public @interface ActionValid {
  /**
   * 유효성 검사 실패 시 출력될 메시지를 설정합니다.
   *
   * @return 유효성 검사 실패 메시지
   */
  String message() default "잘못된 거래 종류";
  
  /**
   * 빈 그룹 배열을 반환합니다.
   *
   * @return 그룹 배열
   */
  Class<?>[] groups() default {};
  
  /**
   * 빈 payload 배열을 반환합니다.
   *
   * @return payload 배열
   */
  Class<? extends Payload>[] payload() default {};
}