package com.damo.server.domain.transaction.dto;

import com.damo.server.domain.transaction.entity.TransactionAction;

import java.time.LocalDateTime;

public record RequestUpdateTransactionDto(
        Long personId,
        LocalDateTime eventDate,
        Integer amount,
        String memo,
        TransactionAction action
) {
}
