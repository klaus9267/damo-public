package com.damo.server.application.handler.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseCustomException {
    private final int statusCode;
    private final String statusMessage;
    private final String message;

    public static ResponseCustomException of(final CustomException customException) {
        return new ResponseCustomException(customException.getCustomErrorCode().getStatusCode(), customException.getCustomErrorCode().getMessage(), customException.getMessage());
    }
}
