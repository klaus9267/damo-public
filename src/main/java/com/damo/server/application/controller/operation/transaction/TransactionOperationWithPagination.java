package com.damo.server.application.controller.operation.transaction;

import com.damo.server.application.handler.error.BadRequestError;
import com.damo.server.application.handler.error.InternalServerError;
import com.damo.server.application.handler.error.UnauthorizedError;
import com.damo.server.domain.transaction.dto.TransactionPaginationResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.converters.models.PageableAsQueryParam;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "페이지네이션 처리된 데이터 응답",
                        content = @Content(schema = @Schema(implementation = TransactionPaginationResponseDto.class))),
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
@PageableAsQueryParam
public @interface TransactionOperationWithPagination {
    String summary() default "";

    String description() default "";
}
