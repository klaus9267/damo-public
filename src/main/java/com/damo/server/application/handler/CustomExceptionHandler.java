package com.damo.server.application.handler;


import com.damo.server.application.handler.error.ExceptionError;
import com.damo.server.application.handler.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    /* Validator Exception */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidException(final MethodArgumentNotValidException ex) {
        log.warn("Validator Exception: ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildExceptionError(ex, HttpStatus.BAD_REQUEST));
    }

    /* 400 - Bad Request */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(final RuntimeException ex) {
        log.warn("Bad Request: ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildExceptionError(ex, HttpStatus.BAD_REQUEST));
    }

    /* 401 - Unauthorized */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorized(final RuntimeException ex) {
        log.warn("Unauthorized: ", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(buildExceptionError(ex, HttpStatus.UNAUTHORIZED));
    }

    /* 403 - Forbidden */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbidden(final RuntimeException ex) {
        log.warn("Forbidden: ", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(buildExceptionError(ex, HttpStatus.FORBIDDEN));
    }

    /* 404 - Not Found */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(final RuntimeException ex) {
        log.warn("Not Found: ", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildExceptionError(ex, HttpStatus.NOT_FOUND));
    }

    /* 500 - Internal Server Error(and ALL) */
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<?> handleServerError(final Exception ex) {
        log.info(ex.getClass().getName());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildExceptionError(ex, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /* 알 수 없는 에러 */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleUnknownException(final RuntimeException ex) {
        // 임시 Enum 에러 핸들링 분기, TODO: enum 에러 메시지 가공
        if(ex.getMessage().contains("Enum class")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildExceptionError(ex, HttpStatus.BAD_REQUEST));
        }
        log.warn("알 수 없음, ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildExceptionError(ex, HttpStatus.INTERNAL_SERVER_ERROR));
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