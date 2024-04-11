package com.damo.server.application.handler.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * {@code ResponseCustomException}은 예외 응답을 나타내는 클래스입니다.
 */
@Getter
@AllArgsConstructor
public class ResponseCustomException {
  private final int statusCode;
  private final String statusMessage;
  private final String message;

  /**
   * {@code CustomException}에서 {@code ResponseCustomException}으로 변환하는 정적 메서드입니다.
   *
   * @param customException 변환할 사용자 정의 예외
   * @return 변환된 응답 예외 객체
   */
  public static ResponseCustomException of(final CustomException customException) {
    return new ResponseCustomException(
        customException.getCustomErrorCode().getStatusCode(),
        customException.getCustomErrorCode().getMessage(),
        customException.getMessage()
    );
  }
}
