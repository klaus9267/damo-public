package com.damo.server.application.controller.validation.transaction;

import com.damo.server.domain.transaction.entity.TransactionAction;
import com.damo.server.domain.transaction.entity.TransactionAmount;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ActionValidator implements ConstraintValidator<TransactionActionValid, TransactionAmount> {
    @Override
    public boolean isValid(TransactionAmount transaction, ConstraintValidatorContext context) {
        return transaction.getAction() != TransactionAction.TOTAL;
    }
}

