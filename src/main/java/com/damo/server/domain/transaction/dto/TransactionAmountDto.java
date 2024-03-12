package com.damo.server.domain.transaction.dto;

import com.damo.server.application.controller.validation.transaction.ActionValid;
import com.damo.server.domain.transaction.entity.TransactionAction;
import com.damo.server.domain.transaction.entity.TransactionAmount;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TransactionAmountDto(
    @NotNull
    @ActionValid
    @Schema(description = "거래 종류", example = "GIVING")
    TransactionAction action,
    @NotNull
    @Min(value = 0)
    @Schema(description = "거래 금액", example = "50000")
    Long amount
) {
  public static TransactionAmountDto from(final TransactionAmount transactionAmount) {
    return new TransactionAmountDto(transactionAmount.getAction(), transactionAmount.getAmount());
  }
}
