package com.damo.server.domain.transaction.service;

import com.damo.server.application.config.user_details.SecurityUserUtil;
import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import com.damo.server.domain.common.pagination.param.TransactionPaginationParam;
import com.damo.server.domain.transaction.TransactionRepository;
import com.damo.server.domain.transaction.TransactionTotalAmount;
import com.damo.server.domain.transaction.dto.TransactionPaginationResponseDto;
import com.damo.server.domain.transaction.dto.TransactionWithScheduleDto;
import com.damo.server.domain.transaction.entity.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@AllArgsConstructor
@Service
public class TransactionReadService {
    private final TransactionRepository transactionRepository;
    private final SecurityUserUtil securityUserUtil;

    public TransactionTotalAmount readTotalAmounts() {
        return transactionRepository.findTotalAmount(securityUserUtil.getId());
    }

    public TransactionTotalAmount readRecentAmounts(final LocalDateTime startedAt) {
        // controller에서 @Past(message) 작동되도록 변경
        if (startedAt != null && startedAt.isAfter(LocalDateTime.now())) {
            throw new CustomException(CustomErrorCode.BAD_REQUEST, "startedAt must be past or null");
        }
        final LocalDateTime startDate = startedAt == null ? LocalDateTime.now().minusMonths(1) : startedAt;
        return transactionRepository.readRecentAmounts(securityUserUtil.getId(), startDate);
    }

    public TransactionWithScheduleDto readTransaction(final Long transactionId) {
        final Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, securityUserUtil.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND, "조회할 거래 내역을 찾을 수 없음"));
        return TransactionWithScheduleDto.from(transaction);
    }

    public TransactionPaginationResponseDto readTransactionList(final TransactionPaginationParam param) {
        final Page<TransactionWithScheduleDto> transactionPage = transactionRepository.findAllByUserId(param.toPageable(), securityUserUtil.getId(), param.getStartedAt(), param.getEndedAt(), param.getAction());
        return TransactionPaginationResponseDto.from(transactionPage);
    }
}
