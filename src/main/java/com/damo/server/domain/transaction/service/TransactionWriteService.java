package com.damo.server.domain.transaction.service;

import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
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
    public void save(final RequestCreateTransactionDto transactionDto, final Long userId) {
        if (transactionRepository.existsByEventDateAndPersonIdAndEvent(transactionDto.eventDate(), transactionDto.personId(), transactionDto.event())) {
            throw new CustomException(CustomErrorCode.BAD_REQUEST, "내역에서 동일한 기록이 존재");
        }
        transactionRepository.save(Transaction.from(transactionDto, userId));
    }

    @Transactional
    public void patchTransactionById(final RequestUpdateTransactionDto transactionDto, final Long transactionId, final Long userId) {
        final Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, userId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND, "수정할 내역을 찾을 수 없음"));
        transaction.changeInfo(transactionDto);
    }

    @Transactional
    public void removeTransactionById(final Long transactionId, final Long userId) {
        transactionRepository.findByIdAndUserId(transactionId, userId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND, "삭제할 내역을 찾을 수 없음"));
        transactionRepository.deleteById(transactionId);
    }
}
