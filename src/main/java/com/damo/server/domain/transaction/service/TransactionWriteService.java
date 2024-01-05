package com.damo.server.domain.transaction.service;

import com.damo.server.application.handler.exception.BadRequestException;
import com.damo.server.application.handler.exception.NotFoundException;
import com.damo.server.domain.schedule.Schedule;
import com.damo.server.domain.schedule.ScheduleRepository;
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
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public void save(final RequestCreateTransactionDto transactionDto, final Long userId) {
        if (transactionRepository.existsByPersonId(transactionDto.eventDate(), transactionDto.personId(), transactionDto.event())) {
            throw new BadRequestException("내역에서 동일한 기록이 존재");
        }
        scheduleRepository.save(Schedule.from(transactionDto, userId));
    }

    @Transactional
    public void patchTransactionById(final RequestUpdateTransactionDto transactionDto, final Long transactionId, final Long userId) {
        final Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, userId).orElseThrow(() -> new NotFoundException("수정할 내역을 찾을 수 없음"));
        transaction.changeInfo(transactionDto);
    }

    @Transactional
    public void removeTransactionById(final Long transactionId, final Long userId) {
        transactionRepository.findByIdAndUserId(transactionId, userId).orElseThrow(() -> new NotFoundException("삭제할 내역을 찾을 수 없음"));
        transactionRepository.deleteById(transactionId);
    }
}
