package com.damo.server.domain.transaction.dto;

import com.damo.server.application.controller.validation.transaction.ActionValid;
import com.damo.server.domain.transaction.entity.TransactionAction;
import com.damo.server.domain.transaction.entity.TransactionCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * {@code RequestCreateTransactionDto}는 내역 생성 요청을 나타내는 DTO 클래스입니다.
 */
public record RequestCreateTransactionDto(
    @NotNull(message = "personId is required")
    @Schema(description = "대상 id", example = "1")
    Long personId,
    
    @NotNull(message = "eventDate is required")
    @Schema(description = "거래 날짜", example = "2024-01-02T12:34:56")
    LocalDateTime eventDate,
    
    @NotEmpty(message = "event is required")
    @Schema(description = "행사 이름", example = "민호 취업 축하 파티")
    String event,
    
    @NotNull
    @ActionValid
    @Schema(description = "거래 종류", example = "GIVING")
    TransactionAction action,
    
    @NotNull
    @Schema(description = "거래 금액", example = "50000")
    Long amount,
    
    @NotNull
    @Schema(description = "선물 종류 CASH | GIFT | MOBILE_VOUCHER | ETC", example = "CASH")
    TransactionCategory category,
    
    @Length(max = 200, message = "memo max length 200")
    @Schema(description = "없거나 최대 200자", example = "메모가 사는 곳은 메모도 메모시 memory 엌ㅋㅋㅋㅋ")
    String memo
) {
}