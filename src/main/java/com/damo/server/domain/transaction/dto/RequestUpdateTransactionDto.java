package com.damo.server.domain.transaction.dto;

import com.damo.server.domain.transaction.entity.TransactionAmount;
import com.damo.server.domain.transaction.entity.TransactionCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record RequestUpdateTransactionDto(
        @NotNull(message = "personId is required")
        @Schema(description = "대상 id", example = "1")
        Long personId,

        @Valid
        @Schema(description = "거래 종류 GIVING | RECEIVING")
        TransactionAmount transactionAmount,

        @Schema(description = "선물 종류 CASH | GIFT | MOBILE_VOUCHER | ETC", example = "CASH")
        TransactionCategory category,

        @Length(max = 200, message = "memo max length 200")
        @Schema(description = "없거나 최대 200자", example = "메모가 사는 곳은 메모도 메모시 memory 엌ㅋㅋㅋㅋ")
        String memo
) {
}
