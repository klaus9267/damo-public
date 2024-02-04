package com.damo.server.application.controller.operation.oauth;

import com.damo.server.application.handler.exception.ResponseCustomException;
import com.damo.server.domain.person.dto.RequestCreatePersonDto;
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

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
  requestBody = @RequestBody(
    content = @Content(
      schema = @Schema(
        implementation = RequestCreatePersonDto.class
      )
    )
  ),
  responses = {
    @ApiResponse(responseCode = "302", description = "OAuth 로그인 페이지로 리다이렉트"),
    @ApiResponse(
      responseCode = "400",
      description = "BAD REQUEST",
      content = @Content(
        examples = { @ExampleObject(name = "카카오, 네이버, 구글 외 provider 입력", value = "{\"statusCode\" : 400, \"statusMessage\" : \"Bad Request\", \"message\" : \"provider validation 에러\" }") },
        schema = @Schema(implementation = ResponseCustomException.class)
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "UNAUTHORIZED",
      content = @Content(
        examples = { @ExampleObject(name = "지원하지 않는 provider", value = "{\"statusCode\" : 401, \"statusMessage\" : \"Unauthorized\", \"message\" : \"지원하지 않는 소셜 로그인\" }") },
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
public @interface OAuthProviderOperation {
  String summary() default "";
  String description() default "";
}
