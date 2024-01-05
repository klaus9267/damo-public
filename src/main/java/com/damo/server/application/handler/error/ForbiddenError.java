package com.damo.server.application.handler.error;

import io.swagger.v3.oas.annotations.media.Schema;

public record ForbiddenError(
        @Schema(example = "forbidden message") String message,
        @Schema(example = "FORBIDDEN") String statusMessage,
        @Schema(example = "403") Integer statusCode
) {}
