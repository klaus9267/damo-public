package com.damo.server.domain.person.dto;

import com.damo.server.application.controller.validation.person.PersonRelationValid;
import com.damo.server.domain.person.entity.PersonRelation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record RequestUpdatePersonDto(
        @Schema(description = "1자 ~ 20자 사이", example = "홍길동")
        @NotBlank(message = "name is required")
        @Length(min = 1, max = 20, message = "name length 1 ~ 20")
        String name,

        @PersonRelationValid(enumClass = PersonRelation.class)
        @Schema(description = "1자 ~ 5자 사이", example = "가족")
        @NotNull
        PersonRelation relation,

        @Schema(description = "없거나 최대 200자", example = "결혼식장 뷔페 노맛")
        @Length(max = 200, message = "memo max length 200")
        String memo,

        @Schema(description = "9자 혹은 11자 번호(ex: 020000000, 0211112222, 01011112222", example = "01055553333")
        @Pattern(regexp = "^\\d{9,11}$", message = "a contact is a 9 or 11-digit range containing only numbers")
        String contact
) {}
