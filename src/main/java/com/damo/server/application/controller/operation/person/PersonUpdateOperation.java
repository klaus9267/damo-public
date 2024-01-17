package com.damo.server.application.controller.operation.person;

import com.damo.server.application.handler.exception.ResponseCustomException;
import com.damo.server.domain.person.dto.RequestUpdatePersonDto;
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
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RequestUpdatePersonDto.class))),
        responses = {
                @ApiResponse(responseCode = "204", description = "성공적으로 반영됨"),
                @ApiResponse(
                        responseCode = "400",
                        description = "BAD REQUEST",
                        content = @Content(
                                examples = {
                                        @ExampleObject(name = "잘못된 person id", value = "{\"statusCode\" : 400, \"statusMessage\" : \"Bad Request\", \"message\" : \"bad request\" }"),
                                        @ExampleObject(name = "잘못된 body", value = "{\"statusCode\" : 400, \"statusMessage\" : \"Bad Request\", \"message\" : \"bad request\" }")
                                },
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
                        description = "NOT_FOUND",
                        content = @Content(
                                examples = { @ExampleObject(name = "수정할 대상 없음", value = "{\"statusCode\" : 404, \"statusMessage\" : \"Unauthorized\", \"message\" : \"수정할 대상을 찾을 수 없음\" }") },
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
public @interface PersonUpdateOperation {
    String summary() default "";
    String description() default "";
}
