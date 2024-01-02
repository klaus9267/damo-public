package com.damo.server.application.controller.validation.transaction;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ActionValidator.class)
public @interface ActionValid {
    String message() default "잘못된 거래 종류";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}