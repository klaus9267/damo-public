package com.damo.server.application.controller.operation.person;

import com.damo.server.application.handler.error.*;
import com.damo.server.domain.person.dto.RequestCreatePersonDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RequestCreatePersonDto.class))),
        responses = {
                @ApiResponse(responseCode = "204", description = "성공적으로 반영됨"),
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
                        responseCode = "500",
                        description = "INTERNAL_SERVER_ERROR",
                        content = @Content(schema = @Schema(implementation = InternalServerError.class))
                )
        }
)
public @interface CreatePersonOperationWithBody {
    String summary() default "";
    String description() default "";
}
