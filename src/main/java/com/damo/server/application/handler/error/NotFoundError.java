package com.damo.server.application.handler.error;

import io.swagger.v3.oas.annotations.media.Schema;

public record NotFoundError(
        @Schema(example = "not found message") String message,
        @Schema(example = "NOT_FOUND") String statusMessage,
        @Schema(example = "404") Integer statusCode
) {}
