package com.damo.server.application.handler.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final CustomErrorCode customErrorCode;
    private final String message;

    public CustomException(final CustomErrorCode customErrorCode, final String message) {
        super(message);
        this.customErrorCode = customErrorCode;
        this.message = message;
    }
}
