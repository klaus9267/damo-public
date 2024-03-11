package com.damo.server.common;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

public class PaginationTestParameter {
  private final MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

  private PaginationTestParameter() {
    this.parameters.add("page", "0");
    this.parameters.add("size", "10");
  }

  public static PaginationTestParameter getInitialParams() {
    return new PaginationTestParameter();
  }

  public PaginationTestParameter put(String key, String value) {
    this.parameters.put(key, List.of(value));
    return this;
  }

  public PaginationTestParameter setPage(final Integer page) {
    this.parameters.set("page", String.valueOf(page));
    return this;
  }

  public PaginationTestParameter setSize(final Integer size) {
    this.parameters.set("size", String.valueOf(size));
    return this;
  }

  public MultiValueMap<String, String> build() {
    return parameters;
  }
}
