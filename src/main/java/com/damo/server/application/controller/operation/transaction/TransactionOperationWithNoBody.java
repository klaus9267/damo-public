package com.damo.server.application.controller.operation.transaction;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Operation(responses = { @ApiResponse(responseCode = "204", description = "응답 없음") })
public @interface TransactionOperationWithNoBody {
    String summary() default "";
    String description() default "";
}
