package com.damo.server.application.handler.error;

import io.swagger.v3.oas.annotations.media.Schema;

public record BadRequestError (
    @Schema(example = "bad request message") String message,
    @Schema(example = "BAD_REQUEST") String statusMessage,
    @Schema(example = "400") Integer statusCode
) {}
