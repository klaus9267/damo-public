package com.damo.server.application.handler.error;

import io.swagger.v3.oas.annotations.media.Schema;

public record InternalServerError(
        @Schema(example = "internal server message") String message,
        @Schema(example = "INTERNAL_SERVER_ERROR") String statusMessage,
        @Schema(example = "500") Integer statusCode
) {}
