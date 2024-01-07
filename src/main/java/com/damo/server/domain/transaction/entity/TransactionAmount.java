package com.damo.server.domain.transaction.entity;

import com.damo.server.application.controller.validation.transaction.ActionValid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(force = true)
public class TransactionAmount {
    private static final Long DEFAULT_AMOUNT = 0L;

    @Enumerated(EnumType.STRING)
    @NotNull
    @ActionValid
    @Schema(description = "거래 종류", example = "GIVING")
    private final TransactionAction action;

    @NotNull(message = "amount is required")
    @Schema(description = "거래 금액", example = "50000")
    private final Long amount;

    public TransactionAmount(Long amount, String action) {
        this.amount = amount == null ? DEFAULT_AMOUNT : amount;
        this.action = TransactionAction.valueOf(action);
    }
}
