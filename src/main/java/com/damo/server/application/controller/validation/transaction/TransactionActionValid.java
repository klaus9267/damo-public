package com.damo.server.application.controller.validation.transaction;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ActionValidator.class)
public @interface TransactionActionValid {
    String message() default "잘못된 거래 종류";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}