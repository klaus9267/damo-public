package com.damo.server.domain.person.dto;

import jakarta.validation.constraints.*;

public record RequestPersonDto(
        @NotBlank(message = "name is required")
        @Size(min = 1, max = 20, message = "name length 1 ~ 20")
        String name,

        @NotBlank(message = "relation is required")
        @Size(min = 1, max = 5, message = "relation length 1 ~ 5")
        String relation, // TODO: Enum 발리데이터로 수정

        @Null
        @Max(value = 200, message = "memo max length 200")
        String memo,

        @NotNull(message = "userId is null")
        @Positive(message = "userId not positive")
        Long userId
) {}
