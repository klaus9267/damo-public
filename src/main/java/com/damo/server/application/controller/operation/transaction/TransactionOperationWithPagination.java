package com.damo.server.application.controller.operation.transaction;

import com.damo.server.domain.person.dto.PeoplePaginationResponseDto;
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
        responses = { @ApiResponse(responseCode = "200", description = "페이지네이션 처리된 데이터 응답", content = @Content(schema = @Schema(implementation = PeoplePaginationResponseDto.class))) }
)
@PageableAsQueryParam
public @interface TransactionOperationWithPagination {
    String summary() default "";
    String description() default "";
}
