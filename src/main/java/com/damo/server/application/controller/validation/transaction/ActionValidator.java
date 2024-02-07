package com.damo.server.application.controller.validation.transaction;

import com.damo.server.domain.transaction.entity.TransactionAction;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * {@code ActionValidator}는 {@link ActionValid} 어노테이션을 사용하여
 * {@link TransactionAction} 객체의 유효성을 검사하는 ConstraintValidator 구현체입니다.
 */
public class ActionValidator implements ConstraintValidator<ActionValid, TransactionAction> {
  @Override
  public boolean isValid(final TransactionAction action, final ConstraintValidatorContext context) {
    return !TransactionAction.TOTAL.equals(action);
  }
}

