package com.damo.server.domain.transaction;

import com.damo.server.application.handler.exception.BadRequestException;
import com.damo.server.application.handler.exception.NotFoundException;
import com.damo.server.domain.common.pagination.param.TransactionPaginationParam;
import com.damo.server.domain.transaction.dto.RequestTransactionDto;
import com.damo.server.domain.transaction.dto.TransactionDto;
import com.damo.server.domain.transaction.entity.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@AllArgsConstructor
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    @Transactional
    public void save(final RequestTransactionDto scheduleDto, final Long userId) {
        if (transactionRepository.existsByEventDateAndEventAndPersonId(scheduleDto.eventDate(), scheduleDto.event(), scheduleDto.personId())) {
            throw new BadRequestException("스케줄 내에서 동일한 기록이 존재");
        }
        transactionRepository.save(Transaction.from(scheduleDto, userId));
    }

    public ScheduleAmount readTotalAmounts(final Long userId) {
        return transactionRepository.findTotalAmount(userId);
    }

    public ScheduleAmount readRecentAmounts(final Long userId, final LocalDateTime startedAt) {
        return transactionRepository.readRecentAmounts(userId, startedAt);
    }

    public TransactionDto readTransaction(final Long transactionId, final Long userId) {
        final Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, userId).orElseThrow(() -> new NotFoundException("조회할 대상을 찾을 수 없음"));
        return TransactionDto.from(transaction);
    }

    public Page<TransactionDto> readTransactionList(final TransactionPaginationParam param, final Long userId) {
        return transactionRepository.findAllByUserId(param.toPageable(), userId, param.getStartedAt(), param.getEndedAt(), param.getAction());
    }

    @Transactional
    public void patchTransactionById(final RequestTransactionDto scheduleDto, final Long transactionId, final Long userId) {
        final Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, userId).orElseThrow(() -> new NotFoundException("수정할 대상을 찾을 수 없음"));
        transaction.changeInfo(scheduleDto);
    }

    public void removeTransactionById(final Long transactionId, final Long userId) {
        transactionRepository.findByIdAndUserId(transactionId, userId).orElseThrow(() -> new NotFoundException("조회할 대상을 찾을 수 없음"));
        transactionRepository.deleteById(transactionId);
    }
}
