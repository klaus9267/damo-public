package com.damo.server.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RequiredArgsConstructor
@Getter
public class PaginationTestParameter {
  private final MultiValueMap<String, String> parameters;

  private PaginationTestParameter() {
    this.parameters = new LinkedMultiValueMap<>();
    this.parameters.add("page", "0");
    this.parameters.add("size", "10");
  }

  public static MultiValueMap<String, String> getInitialParams() {
    return new PaginationTestParameter().getParameters();
  }
}
