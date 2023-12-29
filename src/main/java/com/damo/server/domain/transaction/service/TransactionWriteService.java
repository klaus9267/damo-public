package com.damo.server.domain.transaction.service;

import com.damo.server.application.handler.exception.BadRequestException;
import com.damo.server.application.handler.exception.NotFoundException;
import com.damo.server.domain.transaction.TransactionRepository;
import com.damo.server.domain.transaction.dto.RequestCreateTransactionDto;
import com.damo.server.domain.transaction.dto.RequestUpdateTransactionDto;
import com.damo.server.domain.transaction.entity.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class TransactionWriteService {
    private final TransactionRepository transactionRepository;

    @Transactional
    public void save(final RequestCreateTransactionDto scheduleDto, final Long userId) {
        if (transactionRepository.existsByEventDateAndPersonId(scheduleDto.eventDate(), scheduleDto.personId())) {
            throw new BadRequestException("스케줄 내에서 동일한 기록이 존재");
        }
        transactionRepository.save(Transaction.from(scheduleDto, userId));
    }

    @Transactional
    public void patchTransactionById(final RequestUpdateTransactionDto scheduleDto, final Long transactionId, final Long userId) {
        final Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, userId).orElseThrow(() -> new NotFoundException("수정할 대상을 찾을 수 없음"));
        transaction.changeInfo(scheduleDto);
    }

    public void removeTransactionById(final Long transactionId, final Long userId) {
        transactionRepository.findByIdAndUserId(transactionId, userId).orElseThrow(() -> new NotFoundException("조회할 대상을 찾을 수 없음"));
        transactionRepository.deleteById(transactionId);
    }
}
