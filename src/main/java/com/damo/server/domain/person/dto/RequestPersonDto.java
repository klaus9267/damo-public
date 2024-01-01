package com.damo.server.domain.person.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

@Schema(title = "대상 요청 스키마")
public record RequestPersonDto(
        // TODO: 문자열 검증 패턴 구현해야 함
        @NotBlank(message = "name is required")
        @Length(min = 1, max = 20, message = "name length 1 ~ 20")
        @Schema(description = "1자 ~ 20자 사이", example = "홍길동")
        String name,

        @NotBlank(message = "relation is required")
        @Length(min = 1, max = 5, message = "relation length 1 ~ 5")
        @Schema(description = "1자 ~ 5자 사이", example = "가족")
        String relation,

        @Length(max = 200, message = "memo max length 200")
        @Schema(description = "없거나 최대 200자", example = "결혼식장 뷔페 노맛")
        String memo,

        @Pattern(regexp =  "^\\d{9,11}$", message = "a contact is a 9 or 11-digit range containing only numbers")
        @Schema(description = "9자 혹은 11자 번호(ex: 020000000, 0211112222, 01011112222", example = "01055553333")
        String contact
) {}
