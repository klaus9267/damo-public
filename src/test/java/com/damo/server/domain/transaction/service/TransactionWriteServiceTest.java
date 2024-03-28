package com.damo.server.domain.transaction.service;

import com.damo.server.application.handler.exception.CustomException;
import com.damo.server.application.security.user_details.SecurityUserUtil;
import com.damo.server.common.WithMockCustomUser;
import com.damo.server.domain.transaction.TransactionRepository;
import com.damo.server.domain.transaction.dto.RequestCreateTransactionDto;
import com.damo.server.domain.transaction.dto.RequestUpdateTransactionDto;
import com.damo.server.domain.transaction.dto.TransactionAmountDto;
import com.damo.server.domain.transaction.entity.Transaction;
import com.damo.server.domain.transaction.entity.TransactionAction;
import com.damo.server.domain.transaction.entity.TransactionCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionWriteServiceTest {
  TransactionWriteService transactionWriteService;
  @Mock
  TransactionRepository transactionRepository;
  @Mock
  SecurityUserUtil securityUserUtil;

  @BeforeEach
  void set_up() {
    transactionWriteService = new TransactionWriteService(transactionRepository, securityUserUtil);
  }

  @Nested
  @DisplayName("성공 케이스")
  class 성공 {
    LocalDateTime now;
    Long userId = 1L;
    Long transactionId = 1L;

    @BeforeEach
    void 초기값() {
      now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
      when(securityUserUtil.getId()).thenReturn(userId);
    }

    @Test
    void 내역_생성_선물_종류() {
      final TransactionCategory[] categories = TransactionCategory.values();
      final TransactionAmountDto amountDto = new TransactionAmountDto(TransactionAction.GIVING, 1000L);
      final Transaction transaction = new Transaction();

      when(transactionRepository.existsByEventDateAndPersonIdAndEvent(any(LocalDateTime.class), anyLong(), anyString())).thenReturn(false);
      when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

      for (final TransactionCategory category : categories) {
        final RequestCreateTransactionDto createTransactionDto = new RequestCreateTransactionDto(1L, now, "event", amountDto, category, "memo");
        transactionWriteService.save(createTransactionDto);
      }

      verify(transactionRepository, times(categories.length)).existsByEventDateAndPersonIdAndEvent(any(LocalDateTime.class), anyLong(), anyString());
      verify(transactionRepository, times(categories.length)).save(any(Transaction.class));
    }

    @Test
    void 내역_생성_거래_종류() {
      final TransactionAction[] actions = TransactionAction.values();
      final Transaction transaction = new Transaction();

      when(transactionRepository.existsByEventDateAndPersonIdAndEvent(any(LocalDateTime.class), anyLong(), anyString())).thenReturn(false);
      when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

      for (final TransactionAction action : actions) {
        if (TransactionAction.TOTAL.equals(action)) {
          continue;
        }
        final TransactionAmountDto amountDto = new TransactionAmountDto(action, 1000L);
        final RequestCreateTransactionDto createTransactionDto = new RequestCreateTransactionDto(1L, now, "event", amountDto, TransactionCategory.ETC, "memo");
        transactionWriteService.save(createTransactionDto);
      }

      verify(transactionRepository, times(actions.length - 1)).existsByEventDateAndPersonIdAndEvent(any(LocalDateTime.class), anyLong(), anyString());
      verify(transactionRepository, times(actions.length - 1)).save(any(Transaction.class));
    }

    @Test
    void 내역_수정_선물_종류() {
      final TransactionCategory[] categories = TransactionCategory.values();
      final TransactionAmountDto amountDto = new TransactionAmountDto(TransactionAction.GIVING, 1000L);
      final Transaction transaction = new Transaction();

      when(transactionRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(transaction));

      for (final TransactionCategory category : categories) {
        final RequestUpdateTransactionDto updateTransactionDto = new RequestUpdateTransactionDto(1L, amountDto, category, "memo");
        transactionWriteService.patchTransactionById(updateTransactionDto, transactionId);
      }

      verify(transactionRepository, times(categories.length)).findByIdAndUserId(anyLong(), anyLong());
      verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void 내역_수정_거래_종류() {
      final TransactionAction[] actions = TransactionAction.values();
      final Transaction transaction = new Transaction();

      when(transactionRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(transaction));

      for (final TransactionAction action : actions) {
        if (TransactionAction.TOTAL.equals(action)) {
          continue;
        }
        final TransactionAmountDto amountDto = new TransactionAmountDto(action, 1000L);
        final RequestUpdateTransactionDto updateTransactionDto = new RequestUpdateTransactionDto(1L, amountDto, TransactionCategory.ETC, "memo");
        transactionWriteService.patchTransactionById(updateTransactionDto, transactionId);
      }

      verify(transactionRepository, times(actions.length - 1)).findByIdAndUserId(anyLong(), anyLong());
      verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void 내역_삭제() {
      final TransactionAction[] actions = TransactionAction.values();
      final Transaction transaction = new Transaction();

      when(transactionRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(transaction));
      doNothing().when(transactionRepository).deleteById(anyLong());

      transactionWriteService.removeTransactionById(transactionId);

      verify(transactionRepository).findByIdAndUserId(anyLong(), anyLong());
      verify(transactionRepository).deleteById(anyLong());
    }
  }

  @Nested
  @DisplayName("실패 케이스")
  class 실패 {
    LocalDateTime now;
    Long transactionId = 1L;

    @BeforeEach
    void 초기값() {
      now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    @Nested
    @DisplayName("내역 생성")
    class 내역_생성_실패 {
      @Test
      void 내역_생성_중복_내역() {
        final TransactionAmountDto amountDto = new TransactionAmountDto(TransactionAction.GIVING, 1000L);
        final RequestCreateTransactionDto createTransactionDto = new RequestCreateTransactionDto(1L, now, "event", amountDto, TransactionCategory.ETC, "memo");

        when(transactionRepository.existsByEventDateAndPersonIdAndEvent(any(LocalDateTime.class), anyLong(), anyString())).thenReturn(true);

        assertThatThrownBy(() -> transactionWriteService.save(createTransactionDto)).isInstanceOf(CustomException.class);

        verify(transactionRepository).existsByEventDateAndPersonIdAndEvent(any(LocalDateTime.class), anyLong(), anyString());
        verify(securityUserUtil, never()).getId();
        verify(transactionRepository, never()).save(any(Transaction.class));
      }

      @Test
      void 내역_수정_없는_내역() {
        final TransactionAmountDto amountDto = new TransactionAmountDto(TransactionAction.GIVING, 1000L);
        final RequestUpdateTransactionDto updateTransactionDto = new RequestUpdateTransactionDto(1L, amountDto, TransactionCategory.ETC, "memo");

        when(transactionRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionWriteService.patchTransactionById(updateTransactionDto, transactionId)).isInstanceOf(CustomException.class);

        verify(transactionRepository).findByIdAndUserId(anyLong(), anyLong());
        verify(transactionRepository, never()).save(any(Transaction.class));
      }

      @Test
      void 내역_삭제_없는_내역() {
        when(transactionRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionWriteService.removeTransactionById(transactionId)).isInstanceOf(CustomException.class);

        verify(transactionRepository).findByIdAndUserId(anyLong(), anyLong());
        verify(transactionRepository, never()).deleteById(anyLong());
      }
    }
  }
}
