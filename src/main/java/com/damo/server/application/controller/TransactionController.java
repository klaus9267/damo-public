package com.damo.server.application.controller;

import com.damo.server.application.config.oauth.PrincipalDetails;
import com.damo.server.domain.common.pagination.param.TransactionPaginationParam;
import com.damo.server.domain.transaction.TransactionAmount;
import com.damo.server.domain.transaction.TransactionService;
import com.damo.server.domain.transaction.dto.RequestTransactionDto;
import com.damo.server.domain.transaction.dto.TransactionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
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
    private final TransactionService transactionService;

    @PostMapping
    @Operation(summary = "내역 생성", description = "응답 없음")
    @ResponseStatus(HttpStatus.CREATED)
    public void addTransaction(
            @RequestBody final RequestTransactionDto scheduleDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        transactionService.save(scheduleDto, principalDetails.getUser().getId());
    }

    @GetMapping("/total-amounts")
    @Operation(summary = "거래 총액 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ResponseEntity<TransactionAmount> readTotalAmounts(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        final TransactionAmount amount = transactionService.readTotalAmounts(principalDetails.getUser().getId());
        return ResponseEntity.ok(amount);
    }

    @GetMapping("/term-amounts")
    @Operation(summary = "최근 거래 총액 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ResponseEntity<TransactionAmount> readRecentAmounts(
            @RequestParam(value = "startedAt", defaultValue = "#{T(java.time.LocalDateTime).now().minusMonths(1)}") final LocalDateTime startedAt,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        final TransactionAmount amount = transactionService.readRecentAmounts(principalDetails.getUser().getId(), startedAt);
        return ResponseEntity.ok(amount);
    }

    @GetMapping("{transactionId}")
    @Operation(summary = "내역 단건 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ResponseEntity<TransactionDto> readTransaction(
            @PathVariable("transactionId") final Long transactionId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        final TransactionDto transactionDto = transactionService.readTransaction(transactionId, principalDetails.getUser().getId());
        return ResponseEntity.ok(transactionDto);
    }

    @GetMapping
    @Operation(summary = "내역 종류별 목록 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    @PageableAsQueryParam
    public ResponseEntity<Page<TransactionDto>> readScheduleList(
            @Valid @Parameter(hidden = true) final TransactionPaginationParam paginationParam,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        final Page<TransactionDto> transaction = transactionService.readTransactionList(paginationParam, principalDetails.getUser().getId());
        return ResponseEntity.ok(transaction);
    }

    @PatchMapping("{transactionId}")
    @Operation(summary = "내역 수정", description = "응답 없음")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void patchTransactionById(
            @RequestBody final RequestTransactionDto scheduleDto,
            @PathVariable("transactionId") final Long transactionId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        transactionService.patchTransactionById(scheduleDto, transactionId, principalDetails.getUser().getId());
    }

    @DeleteMapping("{transactionId}")
    @Operation(summary = "내역 삭제", description = "응답 없음")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTransactionById(
            @PathVariable("transactionId") final Long transactionId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        transactionService.removeTransactionById(transactionId, principalDetails.getUser().getId());
    }
}
