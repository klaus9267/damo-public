package com.damo.server.application.handler;

import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import com.damo.server.application.handler.exception.ResponseCustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

/**
 * {@code CustomExceptionHandler}는 커스텀 예외 처리를 담당하는 클래스입니다.
 */
@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
  /**
   * 커스텀 예외를 처리하는 메서드입니다.
   *
   * @param exception 커스텀 예외
   * @param request   현재 요청에 대한 정보를 나타내는 HttpServletRequest 객체
   * @return 커스텀 예외에 대한 응답
     */
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ResponseCustomException> handleException(
      final CustomException exception,
      final HttpServletRequest request
  ) {
    log.error(
        "[code] {}, [url] {}, [message] {}",
        exception.getCustomErrorCode(),
        request.getRequestURI(),
        exception.getMessage()
    );

    return ResponseEntity
        .status(exception.getCustomErrorCode().getStatusCode())
        .body(ResponseCustomException.of(exception));
  }

  /**
   * 메소드 인자 유효성 검증 예외를 처리하는 메서드입니다.
   *
   * @param exception 메소드 인자 유효성 검증 예외
   * @return 메소드 인자 유효성 검증 예외에 대한 응답
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ResponseCustomException> handleMethodArgumentNotValidException(
      final MethodArgumentNotValidException exception
  ) {
    log.error("[MethodArgumentNotValidException] " + exception.getMessage());

    final CustomException customException = new CustomException(
        CustomErrorCode.BAD_REQUEST, exception.getMessage()
    );
    return ResponseEntity
        .status(customException.getCustomErrorCode().getStatusCode())
        .body(ResponseCustomException.of(customException));
  }

  /**
   * 핸들러 메서드 유효성 검증 예외를 처리하는 메서드입니다.
   *
   * @param exception 핸들러 메서드 유효성 검증 예외
   * @return 핸들러 메서드 유효성 검증 예외에 대한 응답
   */
  @ExceptionHandler(HandlerMethodValidationException.class)
  protected ResponseEntity<ResponseCustomException> handleHandlerMethodValidationException(
      final HandlerMethodValidationException exception
  ) {
    final String message = exception
        .getAllValidationResults()
        .get(0)
        .getResolvableErrors()
        .get(0)
        .getDefaultMessage();
    log.error("[HandlerMethodValidationException] " + message);

    final CustomException customException = new CustomException(
        CustomErrorCode.BAD_REQUEST, message
    );
    return ResponseEntity
        .status(customException.getCustomErrorCode().getStatusCode())
        .body(ResponseCustomException.of(customException));
  }
}