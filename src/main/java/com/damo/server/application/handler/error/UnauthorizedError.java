package com.damo.server.application.handler.error;

import io.swagger.v3.oas.annotations.media.Schema;

public record UnauthorizedError(
        @Schema(example = "unauthorized message") String message,
        @Schema(example = "UNAUTHORIZED") String statusMessage,
        @Schema(example = "401") Integer statusCode
) {}
