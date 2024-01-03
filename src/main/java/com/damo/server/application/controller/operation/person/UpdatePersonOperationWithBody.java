package com.damo.server.application.controller.operation.person;

import com.damo.server.domain.person.dto.RequestUpdatePersonDto;
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
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RequestUpdatePersonDto.class))),
        responses = { @ApiResponse(responseCode = "204", description = "성공적으로 반영됨") }
)
public @interface UpdatePersonOperationWithBody {
    String summary() default "";
    String description() default "";
}
