package com.damo.server.domain.common.pagination;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PaginationValidator implements ConstraintValidator<PaginationValidation, PaginationSortType> {
    private PaginationSortGroup pageSortGroup;

    @Override
    public void initialize(PaginationValidation constraintAnnotation) {
        pageSortGroup = constraintAnnotation.sortGroup();
    }

    @Override
    public boolean isValid(PaginationSortType value, ConstraintValidatorContext context) {
        return pageSortGroup.getSortTypes().contains(value);
    }
}
