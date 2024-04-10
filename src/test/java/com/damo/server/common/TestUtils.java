package com.damo.server.common;

import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;

import java.lang.reflect.Field;

public class TestUtils {
  public static void setField(Object target, String fieldName, Object value) {
    try {
      Field field = target.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new CustomException(CustomErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }
}
