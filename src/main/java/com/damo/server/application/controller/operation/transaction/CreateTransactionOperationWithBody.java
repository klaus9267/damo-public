package com.damo.server.application.controller.operation.transaction;

import com.damo.server.application.handler.error.BadRequestError;
import com.damo.server.application.handler.error.InternalServerError;
import com.damo.server.application.handler.error.UnauthorizedError;
import com.damo.server.domain.transaction.dto.RequestCreateTransactionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RequestCreateTransactionDto.class))),
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
                )}
)
@ResponseStatus(HttpStatus.NO_CONTENT)
public @interface CreateTransactionOperationWithBody {
    String summary() default "";

    String description() default "";
}
