package com.damo.server.application.handler.error;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;

public record ExceptionError(String message, String statusMessage, Integer statusCode) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Builder
    public ExceptionError(String message, String statusMessage, Integer statusCode) {
        this.message = message;
        this.statusMessage = statusMessage;
        this.statusCode = statusCode != null ? statusCode : HttpStatus.INTERNAL_SERVER_ERROR.value();
    }
}
