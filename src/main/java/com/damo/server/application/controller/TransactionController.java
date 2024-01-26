package com.damo.server.application.controller;

import com.damo.server.application.controller.operation.transaction.*;
import com.damo.server.domain.common.pagination.param.TransactionPaginationParam;
import com.damo.server.domain.transaction.TransactionTotalAmount;
import com.damo.server.domain.transaction.dto.RequestCreateTransactionDto;
import com.damo.server.domain.transaction.dto.RequestUpdateTransactionDto;
import com.damo.server.domain.transaction.dto.TransactionPaginationResponseDto;
import com.damo.server.domain.transaction.dto.TransactionWithScheduleDto;
import com.damo.server.domain.transaction.service.TransactionReadService;
import com.damo.server.domain.transaction.service.TransactionWriteService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
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
    @CreateTransactionOperationWithBody(summary = "내역 추가")
    public void addTransaction(@Valid @RequestBody final RequestCreateTransactionDto transactionDto) {
        transactionWriteService.save(transactionDto);
    }

    @GetMapping("/total-amounts")
    @TransactionOperationWithBody(summary = "전체 거래 총액 조회", description = "전체 내역 총액 데이터 응답")
    public ResponseEntity<TransactionTotalAmount> readTotalAmounts() {
        final TransactionTotalAmount amount = transactionReadService.readTotalAmounts();
        return ResponseEntity.ok(amount);
    }

    @GetMapping("/term-amounts")
    @TransactionOperationWithBody(summary = "최근 거래 총액 조회", description = "최근 거래 총액 데이터 응답")
    public ResponseEntity<TransactionTotalAmount> readRecentAmounts(
            @Parameter(description = "조회 시작 날짜(기본값 1달 전) | null 입력 시 기본값으로 조회", example = "2023-12-12T00:00:00")
            @Past(message = "과거 날짜만 입력 가능") @RequestParam(value = "startedAt", required = false) final LocalDateTime startedAt
    ) {
        final TransactionTotalAmount amount = transactionReadService.readRecentAmounts(startedAt);
        return ResponseEntity.ok(amount);
    }

    @GetMapping("{transactionId}")
    @TransactionOperationWithBody(summary = "내역 단건 조회", description = "단 건 조회된 데이터 응답")
    public ResponseEntity<TransactionWithScheduleDto> readTransaction(@PathVariable("transactionId") final Long transactionId) {
        final TransactionWithScheduleDto transactionWithScheduleDto = transactionReadService.readTransaction(transactionId);
        return ResponseEntity.ok(transactionWithScheduleDto);
    }

    @GetMapping
    @TransactionOperationWithPagination(summary = "내역 종류별 목록 조회")
    public ResponseEntity<TransactionPaginationResponseDto> readTransactionList(@ParameterObject @Valid final TransactionPaginationParam paginationParam) {
        final TransactionPaginationResponseDto transactionPage = transactionReadService.readTransactionList(paginationParam);
        return ResponseEntity.ok(transactionPage);
    }

    @PatchMapping("{transactionId}")
    @UpdateTransactionOperationWithBody(summary = "내역 수정")
    public void patchTransactionById(
            @RequestBody final RequestUpdateTransactionDto transactionDto,
            @PathVariable("transactionId") final Long transactionId
    ) {
        transactionWriteService.patchTransactionById(transactionDto, transactionId);
    }

    @DeleteMapping("{transactionId}")
    @TransactionOperationWithNoBody(summary = "내역 삭제")
    public void removeTransactionById(@PathVariable("transactionId") final Long transactionId) {
        transactionWriteService.removeTransactionById(transactionId);
    }
}
