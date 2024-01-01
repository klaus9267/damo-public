package com.damo.server.domain.transaction.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(force = true)
public class TransactionAmount {
    private static final Long DEFAULT_AMOUNT = 0L;

    @Enumerated(EnumType.STRING)
    private final TransactionAction action;
    private final Long amount;

    public TransactionAmount(Long amount, String action) {
        this.amount = amount == null ? DEFAULT_AMOUNT : amount;
        this.action = TransactionAction.valueOf(action);
    }
}
