package com.damo.server.application.controller.validation.person;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code PersonRelationValid} 어노테이션은 {@link PersonRelationValidator}에서 제공하는 유효성 검사를 지정하는 데 사용됩니다.
 * 이 어노테이션은 메서드, 필드 또는 매개변수에 적용할 수 있으며 런타임에 유지됩니다.
 */
@Constraint(validatedBy = PersonRelationValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PersonRelationValid {
  /**
   * 유효성 검사 실패 시 출력될 메시지를 설정합니다.
   *
   * @return 유효성 검사 실패 메시지
   */
  String message() default "Invalid relation. This is not permitted.";

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

  /**
   * Enum 타입의 클래스를 반환합니다.
   *
   * @return Enum 타입의 클래스
   */
  Class<? extends java.lang.Enum<?>> enumClass();
}