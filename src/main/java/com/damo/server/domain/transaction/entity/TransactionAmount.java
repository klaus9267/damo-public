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
    @Enumerated(EnumType.STRING)
    private final TransactionAction action;
    private final Long amount;

    public TransactionAmount(Long amount, String action) {
        this.amount = amount;
        this.action = TransactionAction.valueOf(action);
    }

    public Long getAmount() {
        return this.amount != null ? amount : 0;
    }
}
