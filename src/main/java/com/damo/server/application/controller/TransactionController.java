package com.damo.server.application.controller;

import com.damo.server.application.controller.operation.transaction.*;
import com.damo.server.domain.common.pagination.param.TransactionPaginationParam;
import com.damo.server.domain.transaction.TransactionTotalAmount;
import com.damo.server.domain.transaction.dto.RequestCreateTransactionDto;
import com.damo.server.domain.transaction.dto.RequestUpdateTransactionDto;
import com.damo.server.domain.transaction.dto.TransactionDto;
import com.damo.server.domain.transaction.dto.TransactionPaginationResponseDto;
import com.damo.server.domain.transaction.service.TransactionReadService;
import com.damo.server.domain.transaction.service.TransactionWriteService;
import com.damo.server.domain.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
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
    @CreateTransactionOperationWithBody(summary = "내역 추가")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addTransaction(
            @Valid @RequestBody final RequestCreateTransactionDto transactionDto,
            @AuthenticationPrincipal final UserDto user
    ) {
        transactionWriteService.save(transactionDto, user.getId());
    }

    @GetMapping("/total-amounts")
    @TransactionOperationWithBody(summary = "전체 거래 총액 조회", description = "전체 내역 총액 데이터 응답")
    public ResponseEntity<TransactionTotalAmount> readTotalAmounts(@AuthenticationPrincipal final UserDto user) {
        final TransactionTotalAmount amount = transactionReadService.readTotalAmounts(user.getId());
        return ResponseEntity.ok(amount);
    }

    @GetMapping("/term-amounts")
    @TransactionOperationWithBody(summary = "최근 거래 총액 조회", description = "최근 거래 총액 데이터 응답")
    public ResponseEntity<TransactionTotalAmount> readRecentAmounts(
            @Parameter(description = "조회 시작 날짜(기본값 1달 전)", example = "2023-12-12T00:00:00")
            @RequestParam(value = "startedAt", required = false) final LocalDateTime startedAt,
            @AuthenticationPrincipal final UserDto user
    ) {
        final TransactionTotalAmount amount = transactionReadService.readRecentAmounts(user.getId(), startedAt);
        return ResponseEntity.ok(amount);
    }

    @GetMapping("{transactionId}")
    @TransactionOperationWithBody(summary = "내역 단건 조회", description = "단 건 조회된 데이터 응답")
    public ResponseEntity<TransactionDto> readTransaction(
            @PathVariable("transactionId") final Long transactionId,
            @AuthenticationPrincipal final UserDto user
    ) {
        final TransactionDto transactionDto = transactionReadService.readTransaction(transactionId, user.getId());
        return ResponseEntity.ok(transactionDto);
    }

    @GetMapping
    @TransactionOperationWithPagination(summary = "내역 종류별 목록 조회")
    public ResponseEntity<TransactionPaginationResponseDto> readTransactionList(
            @ParameterObject @Valid final TransactionPaginationParam paginationParam,
            @AuthenticationPrincipal final UserDto user
    ) {
        final TransactionPaginationResponseDto transactionPage = transactionReadService.readTransactionList(paginationParam, user.getId());

        return ResponseEntity.ok(transactionPage);
    }

    @PatchMapping("{transactionId}")
    @UpdateTransactionOperationWithBody(summary = "내역 수정")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void patchTransactionById(
            @RequestBody final RequestUpdateTransactionDto transactionDto,
            @PathVariable("transactionId") final Long transactionId,
            @AuthenticationPrincipal final UserDto user
    ) {
        transactionWriteService.patchTransactionById(transactionDto, transactionId, user.getId());
    }

    @DeleteMapping("{transactionId}")
    @TransactionOperationWithNoBody(summary = "내역 삭제")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTransactionById(
            @PathVariable("transactionId") final Long transactionId,
            @AuthenticationPrincipal final UserDto user
    ) {
        transactionWriteService.removeTransactionById(transactionId, user.getId());
    }
}
