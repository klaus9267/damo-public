package com.damo.server.application.controller.validation.person;

import com.damo.server.domain.person.entity.PersonRelation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

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