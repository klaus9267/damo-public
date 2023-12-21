package com.damo.server.domain.person.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

public record RequestPersonDto(
        // TODO: 문자열 검증 패턴 구현해야 함
        @NotBlank(message = "name is required")
        @Length(min = 1, max = 20, message = "name length 1 ~ 20")
        String name,

        @NotBlank(message = "relation is required")
        @Length(min = 1, max = 5, message = "relation length 1 ~ 5")
        String relation, // TODO: Enum 발리데이터로 수정

        @Length(max = 200, message = "memo max length 200")
        String memo,

        @Pattern(regexp =  "^\\d{10,11}$", message = "a contact is a 10 or 11-digit range containing only numbers")
        String contact
) {}
