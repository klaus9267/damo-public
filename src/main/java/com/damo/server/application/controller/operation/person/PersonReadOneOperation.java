package com.damo.server.application.controller.operation.person;

import com.damo.server.application.handler.exception.ResponseCustomException;
import com.damo.server.domain.person.dto.PersonDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code PersonReadOneOperation} 어노테이션은 대상 단일 조회 데이터 응답을 설명하는 OpenAPI Operation을 정의합니다.
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
  responses = {
    @ApiResponse(
      responseCode = "200",
      description = "정상 데이터 응답",
      content = @Content(schema = @Schema(implementation = PersonDto.class))
    ),
    @ApiResponse(
      responseCode = "400",
      description = "BAD REQUEST",
      content = @Content(
        examples = { @ExampleObject(name = "잘못된 personId", value = "{\"statusCode\" : 400, \"statusMessage\" : \"Bad Request\", \"message\" : \"bad request\" }") },
        schema = @Schema(implementation = ResponseCustomException.class)
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "UNAUTHORIZED",
      content = @Content(
        examples = { @ExampleObject(name = "유저 인증 실패", value = "{\"statusCode\" : 401, \"statusMessage\" : \"Unauthorized\", \"message\" : \"잘못된 토큰 정보\" }") },
        schema = @Schema(implementation = ResponseCustomException.class)
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "NOT FOUND",
      content = @Content(
        examples = { @ExampleObject(name = "대상을 찾을 수 없음", value = "{\"statusCode\" : 404, \"statusMessage\" : \"Not found\", \"message\" : \"대상을 찾을 수 없습니다.\" }") },
        schema = @Schema(implementation = ResponseCustomException.class)
      )
    ),
    @ApiResponse(
      responseCode = "500",
      description = "INTERNAL SERVER EXCEPTION",
      content = @Content(
        examples = { @ExampleObject(name = "서버 에러", value = "{\"statusCode\" : 500, \"statusMessage\" : \"Internal Server Exception\", \"message\" : \"알 수 없는 에러가 발생할 때 500 에러가 표시됩니다.\"  }") },
        schema = @Schema(implementation = ResponseCustomException.class)
      )
    )
  }
)
public @interface PersonReadOneOperation {
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
