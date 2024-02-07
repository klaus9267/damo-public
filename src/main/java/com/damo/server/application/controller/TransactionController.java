package com.damo.server.application.controller;

import com.damo.server.application.controller.operation.transaction.*;
import com.damo.server.domain.common.pagination.param.TransactionPaginationParam;
import com.damo.server.domain.transaction.dto.*;
import com.damo.server.domain.transaction.dto.TransactionTotalAmountDto;
import com.damo.server.domain.transaction.service.TransactionReadService;
import com.damo.server.domain.transaction.service.TransactionWriteService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * {@code Transaction}는 내역 관련 API를 처리하는 컨트롤러 클래스입니다.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/transactions")
@Tag(name = "TRANSACTION")
public class TransactionController {
  private final TransactionReadService transactionReadService;
  private final TransactionWriteService transactionWriteService;

  /**
   * 내역을 추가하는 API입니다.
   *
   * @param transactionDto 추가할 내역 정보 DTO
   * @return HTTP 상태코드 201 (Created)
   */
  @PostMapping
  @TransactionCreateOperation(summary = "내역 추가")
  public ResponseEntity<Void> addTransaction(@Valid @RequestBody final RequestCreateTransactionDto transactionDto) {
    transactionWriteService.save(transactionDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * 전체 거래 총액을 조회하는 API입니다.
   *
   * @return 전체 거래 총액
   */
  @GetMapping("/total-amounts")
  @TransactionReadOperation(summary = "전체 거래 총액 조회", description = "전체 내역 총액 데이터 응답")
  public ResponseEntity<TransactionTotalAmountDto> readTotalAmounts() {
    final TransactionTotalAmountDto amount = transactionReadService.readTotalAmounts();
    return ResponseEntity.ok(amount);
  }

  /**
   * 최근 거래 총액을 조회하는 API입니다.
   *
   * @param startedAt 조회 시작할 과거 날짜
   * @return 최근 거래 총액
   */
  @GetMapping("/term-amounts")
  @TransactionReadOperation(summary = "최근 거래 총액 조회", description = "최근 거래 총액 데이터 응답")
  public ResponseEntity<TransactionTotalAmountDto> readRecentAmounts(
      @Parameter(description = "조회 시작 날짜(기본값 1달 전) | null 입력 시 기본값으로 조회", example = "2023-12-12T00:00:00")
      @Past(message = "과거 날짜만 입력 가능") @RequestParam(value = "startedAt", required = false) final LocalDateTime startedAt
  ) {
    final TransactionTotalAmountDto amount = transactionReadService.readRecentAmounts(startedAt);
    return ResponseEntity.ok(amount);
  }

  /**
   * 내역을 단건 조회하는 API입니다.
   *
   * @param transactionId 내역 식별자
   * @return 내역과 일정 정보 DTO
   */
  @GetMapping("{transactionId}")
  @TransactionReadOperation(summary = "내역 단건 조회", description = "단 건 조회된 데이터 응답")
  public ResponseEntity<TransactionWithScheduleDto> readTransaction(@PathVariable("transactionId") final Long transactionId) {
    final TransactionWithScheduleDto transactionWithScheduleDto = transactionReadService.readTransaction(transactionId);
    return ResponseEntity.ok(transactionWithScheduleDto);
  }

  /**
   * 특정 종류의 내역 목록을 페이지네이션하여 조회하는 API입니다.
   *
   * @param paginationParam 페이지네이션 파라미터 객체
   * @return 내역 목록 페이지네이션 응답 DTO
   */
  @GetMapping
  @TransactionPaginationOperation(summary = "내역 종류별 목록 조회")
  public ResponseEntity<TransactionPaginationResponseDto> readTransactionList(@ParameterObject @Valid final TransactionPaginationParam paginationParam) {
    final TransactionPaginationResponseDto transactionPage = transactionReadService.readTransactionList(paginationParam);
    return ResponseEntity.ok(transactionPage);
  }

  /**
   * 내역을 수정하는 API입니다.
   *
   * @param transactionId  대상 식별자
   * @param transactionDto 수정할 내역 정보 DTO
   * @return HTTP 상태코드 204 (No Content)
   */
  @PatchMapping("{transactionId}")
  @TransactionUpdateOperation(summary = "내역 수정")
  public ResponseEntity<Void> patchTransactionById(
      @RequestBody final RequestUpdateTransactionDto transactionDto,
      @PathVariable("transactionId") final Long transactionId
  ) {
    transactionWriteService.patchTransactionById(transactionDto, transactionId);
    return ResponseEntity.noContent().build();
  }

  /**
   * 내역을 제거하는 API입니다.
   *
   * @param transactionId 대상 식별자
   * @return HTTP 상태코드 204 (No Content)
   */
  @DeleteMapping("{transactionId}")
  @TransactionDeleteOperation(summary = "내역 삭제")
  public ResponseEntity<Void> removeTransactionById(@PathVariable("transactionId") final Long transactionId) {
    transactionWriteService.removeTransactionById(transactionId);
    return ResponseEntity.noContent().build();
  }
}
