package com.damo.server.common;

import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RequiredArgsConstructor
public class PaginationTestParameter {
  private final MultiValueMap<String, String> parameters;

  public static MultiValueMap<String, String> getInitialParams() {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("page", "0");
    params.add("size", "10");
    return params;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private final MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

    private Builder() {
      this.parameters.add("page", "0");
      this.parameters.add("size", "10");
    }

    public Builder setParameterOf(final String key, final String value) {
      this.parameters.set(key, value);
      return this;
    }

    public Builder page(final Integer page) {
      setParameterOf("page", String.valueOf(page));
      return this;
    }

    public Builder size(final Integer size) {
      setParameterOf("size", String.valueOf(size));
      return this;
    }

    public MultiValueMap<String, String> build() {
      return parameters;
    }
  }
}
