package com.damo.server.domain.transaction.dto;

import com.damo.server.domain.transaction.entity.TransactionAmount;

import java.time.LocalDateTime;

public record RequestUpdateTransactionDto(
        Long personId,
        LocalDateTime eventDate,
        TransactionAmount amount,
        String memo
) {
}
