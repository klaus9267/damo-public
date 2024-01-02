package com.damo.server.domain.transaction.dto;

import com.damo.server.application.controller.validation.transaction.TransactionActionValid;
import com.damo.server.domain.transaction.entity.TransactionAmount;

import java.time.LocalDateTime;

public record RequestCreateTransactionDto(
        Long personId,
        LocalDateTime eventDate,
        @TransactionActionValid(message = "잘못된 거래 종류 입력")
        TransactionAmount transaction,
        String memo
) {}