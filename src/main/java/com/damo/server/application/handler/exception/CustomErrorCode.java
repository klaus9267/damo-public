package com.damo.server.application.handler.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * {@code CustomErrorCode}는 사용자 정의 오류 코드를 정의한 열거형(Enumeration) 클래스입니다.
 */
@RequiredArgsConstructor
@Getter
public enum CustomErrorCode {
  BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase()),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase()),
  FORBIDDEN(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase()),
  NOT_FOUND(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase()),
  INTERNAL_SERVER_ERROR(
    HttpStatus.INTERNAL_SERVER_ERROR.value(),
    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()
  );

  private final int statusCode;
  private final String message;
}
