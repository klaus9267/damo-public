package com.damo.server.application.controller.validation.transaction;

import com.damo.server.domain.transaction.entity.TransactionAction;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ActionValidator implements ConstraintValidator<ActionValid, TransactionAction> {
    @Override
    public boolean isValid(final TransactionAction action, final ConstraintValidatorContext context) {
        return !TransactionAction.TOTAL.equals(action);
    }
}

