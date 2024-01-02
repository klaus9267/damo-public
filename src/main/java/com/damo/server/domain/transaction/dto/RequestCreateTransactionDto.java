package com.damo.server.domain.transaction.dto;

import com.damo.server.application.controller.validation.transaction.TransactionActionValid;
import com.damo.server.domain.transaction.entity.TransactionAmount;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record RequestCreateTransactionDto(
        @NotNull(message = "personId is required")
        @Schema(description = "대상 id", example = "1")
        Long personId,

        @NotNull(message = "eventDate is required")
        @Schema(description = "거래 날짜", example = "2024-01-02T12:34:56")
        LocalDateTime eventDate,
        @Valid
        @TransactionActionValid
        TransactionAmount transaction,
        @Length(max = 200, message = "memo max length 200")
        @Schema(description = "없거나 최대 200자", example = "메모가 사는 곳은 메모도 메모시 memory 엌ㅋㅋㅋㅋ")
        String memo
) {}