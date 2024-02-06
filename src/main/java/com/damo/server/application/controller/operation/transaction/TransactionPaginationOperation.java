package com.damo.server.application.controller.operation.transaction;

import com.damo.server.application.handler.exception.ResponseCustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.converters.models.PageableAsQueryParam;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code TransactionPaginationOperation} 어노테이션은 페이지네이션 처리된 데이터 응답을 설명하는 OpenAPI Operation을 정의합니다.
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
    responses = {
        @ApiResponse(
            responseCode = "200",
            description = "페이지네이션 처리된 데이터 응답",
            content = @Content(schema = @Schema(implementation = TransactionPaginationOperation.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "BAD REQUEST",
            content = @Content(
                examples = {@ExampleObject(name = "잘못된 쿼리 스트링", value = "{\"statusCode\" : 400, \"statusMessage\" : \"Bad Request\", \"message\" : \"bad request\" }")},
                schema = @Schema(implementation = ResponseCustomException.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "UNAUTHORIZED",
            content = @Content(
                examples = {@ExampleObject(name = "유저 인증 실패", value = "{\"statusCode\" : 401, \"statusMessage\" : \"Unauthorized\", \"message\" : \"잘못된 토큰 정보\" }")},
                schema = @Schema(implementation = ResponseCustomException.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "INTERNAL SERVER EXCEPTION",
            content = @Content(
                examples = {@ExampleObject(name = "서버 에러", value = "{\"statusCode\" : 500, \"statusMessage\" : \"Internal Server Exception\", \"message\" : \"알 수 없는 에러가 발생할 때 500 에러가 표시됩니다.\"  }")},
                schema = @Schema(implementation = ResponseCustomException.class)
            )
        )
    }
)
@PageableAsQueryParam
public @interface TransactionPaginationOperation {
  /**
   * OpenAPI Operation의 요약(summary) 정보를 설정합니다.
   *
   * @return Operation 요약 정보
   */
  String summary() default "";
  
  /**
   * OpenAPI Operation의 설명(description) 정보를 설정합니다.
   *
   * @return Operation 설명 정보
   */
  String description() default "";
}
