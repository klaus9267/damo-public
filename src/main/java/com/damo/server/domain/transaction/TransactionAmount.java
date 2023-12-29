package com.damo.server.domain.transaction;

import com.damo.server.domain.transaction.entity.TransactionAction;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TransactionAmount {
    private final Long amount;
    private final TransactionAction action;
}
