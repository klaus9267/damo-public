package com.damo.server.application.controller.operation.person;


import com.damo.server.application.handler.error.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Operation(responses = {
        @ApiResponse(responseCode = "204", description = "응답 없음"),
        @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST",
                content = @Content(schema = @Schema(implementation = BadRequestError.class))
        ),
        @ApiResponse(
                responseCode = "401",
                description = "UNAUTHORIZED",
                content = @Content(schema = @Schema(implementation = UnauthorizedError.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND",
                content = @Content(schema = @Schema(implementation = NotFoundError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "INTERNAL_SERVER_ERROR",
                content = @Content(schema = @Schema(implementation = InternalServerError.class))
        )
    }
)
public @interface PersonOperationWithNoBody {
    String summary() default "";
    String description() default "";
}
