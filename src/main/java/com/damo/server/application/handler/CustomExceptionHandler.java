package com.damo.server.application.handler;


import com.damo.server.application.handler.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
    /** custom 예외 핸들러 */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseCustomException> handleException(final CustomException exception, final HttpServletRequest request) {
        log.error("[code] {}, [url] {}, [message] {}", exception.getCustomErrorCode(), request.getRequestURI(), exception.getMessage());

        return ResponseEntity.status(exception.getCustomErrorCode().getStatusCode()).body(ResponseCustomException.of(exception));
    }

    /** validation 예외 핸들러 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ResponseCustomException> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("[MethodArgumentNotValidException] " + exception.getMessage());

        final CustomException customException = new CustomException(CustomErrorCode.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(customException.getCustomErrorCode().getStatusCode()).body(ResponseCustomException.of(customException));
    }
}