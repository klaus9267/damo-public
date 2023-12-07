package com.damo.server.domain.common.pagination;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { PaginationValidator.class })
public @interface PaginationValidation {
    String message() default "올바르지 않은 페이지 정보입니다.";
    PaginationSortGroup sortGroup() default PaginationSortGroup.EMPTY;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
