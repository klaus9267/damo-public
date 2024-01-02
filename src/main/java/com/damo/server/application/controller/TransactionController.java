package com.damo.server.application.controller;

import com.damo.server.application.controller.operation.transaction.TransactionOperationWithBody;
import com.damo.server.application.controller.operation.transaction.TransactionOperationWithNoBody;
import com.damo.server.application.controller.operation.transaction.TransactionOperationWithPagination;
import com.damo.server.domain.common.pagination.param.TransactionPaginationParam;
import com.damo.server.domain.transaction.TransactionTotalAmount;
import com.damo.server.domain.transaction.dto.RequestCreateTransactionDto;
import com.damo.server.domain.transaction.dto.RequestUpdateTransactionDto;
import com.damo.server.domain.transaction.dto.TransactionDto;
import com.damo.server.domain.transaction.dto.TransactionPaginationResponseDto;
import com.damo.server.domain.transaction.service.TransactionReadService;
import com.damo.server.domain.transaction.service.TransactionWriteService;
import com.damo.server.domain.user.dto.UserDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@RestController
@RequestMapping("/api/transactions")
@Tag(name = "TRANSACTION")
public class TransactionController {
    private final TransactionReadService transactionReadService;
    private final TransactionWriteService transactionWriteService;

    @PostMapping
    @TransactionOperationWithBody(summary = "내역 추가")
    public void addTransaction(
            @Valid @RequestBody final RequestCreateTransactionDto scheduleDto,
            @AuthenticationPrincipal final UserDto user
    ) {
        transactionWriteService.save(scheduleDto, user.getId());
    }

    @GetMapping("/total-amounts")
    @TransactionOperationWithBody(summary = "거래 총액 조회")
    public ResponseEntity<TransactionTotalAmount> readTotalAmounts(@AuthenticationPrincipal final UserDto user) {
        final TransactionTotalAmount amount = transactionReadService.readTotalAmounts(user.getId());
        return ResponseEntity.ok(amount);
    }

    @GetMapping("/term-amounts")
    @TransactionOperationWithBody(summary = "최근 거래 총액 조회")
    public ResponseEntity<TransactionTotalAmount> readRecentAmounts(
            @RequestParam(value = "startedAt", defaultValue = "#{T(java.time.LocalDateTime).now().minusMonths(1)}") final LocalDateTime startedAt,
            @AuthenticationPrincipal final UserDto user
    ) {
        final TransactionTotalAmount amount = transactionReadService.readRecentAmounts(user.getId(), startedAt);
        return ResponseEntity.ok(amount);
    }

    @GetMapping("{transactionId}")
    @TransactionOperationWithBody(summary = "내역 단건 조회")
    public ResponseEntity<TransactionDto> readTransaction(
            @PathVariable("transactionId") final Long transactionId,
            @AuthenticationPrincipal final UserDto user
    ) {
        final TransactionDto transactionDto = transactionReadService.readTransaction(transactionId, user.getId());
        return ResponseEntity.ok(transactionDto);
    }

    @GetMapping
    @TransactionOperationWithPagination(summary = "내역 종류별 목록 조회")
    public ResponseEntity<TransactionPaginationResponseDto> readScheduleList(
            @ParameterObject @Valid final TransactionPaginationParam paginationParam,
            @AuthenticationPrincipal final UserDto user
    ) {
        final TransactionPaginationResponseDto transactionPage = transactionReadService.readTransactionList(paginationParam, user.getId());

        return ResponseEntity.ok(transactionPage);
    }

    @PatchMapping("{transactionId}")
    @TransactionOperationWithBody(summary = "내역 수정")
    public void patchTransactionById(
            @RequestBody final RequestUpdateTransactionDto scheduleDto,
            @PathVariable("transactionId") final Long transactionId,
            @AuthenticationPrincipal final UserDto user
    ) {
        transactionWriteService.patchTransactionById(scheduleDto, transactionId, user.getId());
    }

    @DeleteMapping("{transactionId}")
    @TransactionOperationWithNoBody(summary = "내역 삭제")
    public void removeTransactionById(
            @PathVariable("transactionId") final Long transactionId,
            @AuthenticationPrincipal final UserDto user
    ) {
        transactionWriteService.removeTransactionById(transactionId, user.getId());
    }
}
