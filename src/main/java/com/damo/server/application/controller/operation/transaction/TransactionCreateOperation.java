package com.damo.server.application.controller.operation.transaction;

import com.damo.server.application.handler.exception.ResponseCustomException;
import com.damo.server.domain.transaction.dto.RequestCreateTransactionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code TransactionCreateOperation} 어노테이션은 내역 정보 생성 관련 작업을 설명하는 OpenAPI Operation을 정의합니다.
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RequestCreateTransactionDto.class))),
    responses = {
        @ApiResponse(responseCode = "201", description = "성공적으로 반영됨"),
        @ApiResponse(
            responseCode = "400",
            description = "BAD REQUEST",
            content = @Content(
                examples = {
                    @ExampleObject(name = "동일 날짜, 동일 행사, 동일 이름 입력", value = "{\"statusCode\" : 400, \"statusMessage\" : \"Bad Request\", \"message\" : \"내역에서 동일한 기록이 존재\" }")},
                schema = @Schema(implementation = ResponseCustomException.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "UNAUTHORIZED",
            content = @Content(
                examples = {
                    @ExampleObject(name = "유저 인증 실패", value = "{\"statusCode\" : 401, \"statusMessage\" : \"Unauthorized\", \"message\" : \"잘못된 토큰 정보\" }")},
                schema = @Schema(implementation = ResponseCustomException.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "INTERNAL SERVER EXCEPTION",
            content = @Content(
                examples = {
                    @ExampleObject(name = "서버 에러", value = "{\"statusCode\" : 500, \"statusMessage\" : \"Internal Server Exception\", \"message\" : \"알 수 없는 에러가 발생할 때 500 에러가 표시됩니다.\"  }")},
                schema = @Schema(implementation = ResponseCustomException.class)
            )
        )
    }
)
public @interface TransactionCreateOperation {
  
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
