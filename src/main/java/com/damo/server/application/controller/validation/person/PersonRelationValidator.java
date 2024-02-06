package com.damo.server.application.controller.validation.person;

import com.damo.server.domain.person.entity.PersonRelation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * {@code PersonRelationValidator}는 {@link PersonRelationValid} 어노테이션을 사용하여
 * {@link PersonRelation} 객체의 유효성을 검사하는 ConstraintValidator 구현체입니다.
 */
public class PersonRelationValidator implements ConstraintValidator<PersonRelationValid, PersonRelation> {
  private PersonRelationValid annotation;

  @Override
  public void initialize(final PersonRelationValid constraintAnnotation) {
    this.annotation = constraintAnnotation;
  }

  @Override
  public boolean isValid(final PersonRelation value, final ConstraintValidatorContext context) {
    boolean result = false;
    Object[] enumValues = this.annotation.enumClass().getEnumConstants();
    if (enumValues != null) {
      for (Object enumValue : enumValues) {
        if (value == enumValue) {
          result = true;
          break;
        }
      }
    }
    return result;
  }
}