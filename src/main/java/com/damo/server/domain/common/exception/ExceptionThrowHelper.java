package com.damo.server.domain.common.exception;

import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import java.util.function.Supplier;

/**
 * 이 클래스는 예외를 간편하게 생성하고 발생시키기 위한 도우미 클래스입니다.
 */
public class ExceptionThrowHelper {
  /**
   * 주어진 메시지를 사용하여 NOT_FOUND 타입의 CustomException을 생성하는
   * Supplier<CustomException>를 반환하는 메서드입니다.
   *
   * @param message 생성된 예외에 포함될 메시지
   * @return Supplier<CustomException> 인터페이스를 구현한 람다 표현식
   *         해당 람다는 예외를 생성하고 반환합니다.
   */
  public static Supplier<CustomException> throwNotFound(final String message) {
    return () -> new CustomException(CustomErrorCode.NOT_FOUND, message);
  }
}
