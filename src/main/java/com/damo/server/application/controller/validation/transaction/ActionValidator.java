package com.damo.server.application.controller.validation.transaction;

import com.damo.server.domain.transaction.entity.TransactionAction;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ActionValidator implements ConstraintValidator<ActionValid, TransactionAction> {
    @Override
    public boolean isValid(TransactionAction action, ConstraintValidatorContext context) {
        if (action == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("action is required").addConstraintViolation();
            return false;
        }
        return !action.equals(TransactionAction.TOTAL);
    }
}

