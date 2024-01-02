package com.damo.server.domain.transaction.service;

import com.damo.server.application.handler.exception.NotFoundException;
import com.damo.server.domain.common.pagination.param.TransactionPaginationParam;
import com.damo.server.domain.transaction.TransactionRepository;
import com.damo.server.domain.transaction.TransactionTotalAmount;
import com.damo.server.domain.transaction.dto.TransactionDto;
import com.damo.server.domain.transaction.dto.TransactionPaginationResponseDto;
import com.damo.server.domain.transaction.entity.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@AllArgsConstructor
@Service
public class TransactionReadService {
    private final TransactionRepository transactionRepository;

    public TransactionTotalAmount readTotalAmounts(final Long userId) {
        return transactionRepository.findTotalAmount(userId);
    }

    public TransactionTotalAmount readRecentAmounts(final Long userId, final LocalDateTime startedAt) {
        return transactionRepository.readRecentAmounts(userId, startedAt);
    }

    public TransactionDto readTransaction(final Long transactionId, final Long userId) {
        final Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, userId).orElseThrow(() -> new NotFoundException("조회할 대상을 찾을 수 없음"));
        return TransactionDto.from(transaction);
    }

    public TransactionPaginationResponseDto readTransactionList(final TransactionPaginationParam param, final Long userId) {
        final Page<TransactionDto> transactionPage = transactionRepository.findAllByUserId(param.toPageable(), userId, param.getStartedAt(), param.getEndedAt(), param.getAction());
        return TransactionPaginationResponseDto.from(transactionPage);
    }
}
