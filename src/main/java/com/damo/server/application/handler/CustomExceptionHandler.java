package com.damo.server.application.handler;


import com.damo.server.application.handler.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    /* Validator Exception */
//    @ApiResponse(responseCode = "400", description = "BAD_REQUEST")
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionError handleValidException(final MethodArgumentNotValidException ex) {
        log.warn("Validator Exception: ", ex);
        return buildExceptionError(ex, HttpStatus.BAD_REQUEST);
    }
    /* 400 - Bad Request */
//    @ApiResponse(responseCode = "400", description = "BAD_REQUEST")
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionError handleBadRequest(final RuntimeException ex) {
        log.warn("Bad Request: ", ex);
        return buildExceptionError(ex, HttpStatus.BAD_REQUEST);
    }

    /* 401 - Unauthorized */
//    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ExceptionError handleUnauthorized(final RuntimeException ex) {
        log.warn("Unauthorized: ", ex);
        return buildExceptionError(ex, HttpStatus.UNAUTHORIZED);
    }

    /* 403 - Forbidden */
//    @ApiResponse(responseCode = "403", description = "FORBIDDEN")
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ExceptionError handleForbidden(final RuntimeException ex) {
        log.warn("Forbidden: ", ex);
        return buildExceptionError(ex, HttpStatus.FORBIDDEN);
    }

    /* 404 - Not Found */
//    @ApiResponse(responseCode = "404", description = "NOT_FOUND")
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ExceptionError handleNotFound(final RuntimeException ex) {
        log.warn("Not Found: ", ex);
        return buildExceptionError(ex, HttpStatus.NOT_FOUND);
    }


    /* 500 - Internal Server Error(and ALL) */
//    @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    @ExceptionHandler({ Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ExceptionError handleServerError(final Exception ex) {
        log.info(ex.getClass().getName());
        log.error("Server Error: ", ex);
        return buildExceptionError(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* 알 수 없는 에러 */
//    @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ExceptionError handleUnknownException(final RuntimeException ex) {
        log.warn("알 수 없음, ", ex);
        return buildExceptionError(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ExceptionError buildExceptionError(Exception exception, HttpStatus status) {
        return ExceptionError
                .builder()
                .message(exception.getMessage())
                .statusMessage(status.getReasonPhrase())
                .statusCode(status.value())
                .build();
    }
}