package com.damo.server.application.handler.exception;

import lombok.Getter;

/**
 * {@code CustomException}은 사용자 정의 예외를 나타내는 런타임 예외 클래스입니다.
 */
@Getter
public class CustomException extends RuntimeException {
  private final CustomErrorCode customErrorCode;
  private final String message;

  /**
   * {@code CustomException}의 생성자로, 사용자 정의 오류 코드와 메시지를 받아 객체를 생성합니다.
   *
   * @param customErrorCode 사용자 정의 오류 코드
   * @param message      예외에 대한 추가 메시지
   */
  public CustomException(final CustomErrorCode customErrorCode, final String message) {
    super(message);
    this.customErrorCode = customErrorCode;
    this.message = message;
  }
}
