package com.damo.server.domain.transaction.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class TransactionPaginationResponseDto {
    private final Integer totalPages;
    private final Long totalElements;
    private final List<TransactionDto> transactions;

    private TransactionPaginationResponseDto(final Page<TransactionDto> transactionPage) {
        this.totalPages = transactionPage.getTotalPages();
        this.totalElements = transactionPage.getTotalElements();
        this.transactions = transactionPage.getContent();
    }

    public static TransactionPaginationResponseDto from(final Page<TransactionDto> transactionPage) {
        return new TransactionPaginationResponseDto(transactionPage);
    }
}
