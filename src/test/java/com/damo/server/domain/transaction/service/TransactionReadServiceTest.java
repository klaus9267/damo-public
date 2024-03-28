package com.damo.server.domain.transaction.service;

import com.damo.server.application.security.user_details.SecurityUserUtil;
import com.damo.server.domain.common.pagination.param.TransactionPaginationParam;
import com.damo.server.domain.person.dto.PersonDto;
import com.damo.server.domain.person.entity.Person;
import com.damo.server.domain.person.entity.PersonRelation;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import com.damo.server.domain.schedule.entity.Schedule;
import com.damo.server.domain.schedule.entity.ScheduleStatus;
import com.damo.server.domain.transaction.TransactionRepository;
import com.damo.server.domain.transaction.dto.TransactionAmountDto;
import com.damo.server.domain.transaction.dto.TransactionPaginationResponseDto;
import com.damo.server.domain.transaction.dto.TransactionTotalAmountDto;
import com.damo.server.domain.transaction.dto.TransactionWithScheduleDto;
import com.damo.server.domain.transaction.entity.Transaction;
import com.damo.server.domain.transaction.entity.TransactionAction;
import com.damo.server.domain.transaction.entity.TransactionAmount;
import com.damo.server.domain.transaction.entity.TransactionCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionReadServiceTest {
  TransactionReadService transactionReadService;
  @Mock
  TransactionRepository transactionRepository;
  @Mock
  SecurityUserUtil securityUserUtil;

  @BeforeEach
  void set_up() {
    transactionReadService = new TransactionReadService(transactionRepository, securityUserUtil);
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
    void 내역_전체_거래_총액() {
      final TransactionTotalAmountDto totalAmountDto = new TransactionTotalAmountDto(1000L, 1000L);

      when(transactionRepository.findTotalAmount(anyLong())).thenReturn(totalAmountDto);

      transactionReadService.readTotalAmounts();

      verify(transactionRepository).findTotalAmount(anyLong());
    }

    @Test
    void 내역_최근_거래_총액() {
      final TransactionTotalAmountDto totalAmountDto = new TransactionTotalAmountDto(1000L, 1000L);

      when(transactionRepository.readRecentAmounts(anyLong(), any(LocalDateTime.class))).thenReturn(totalAmountDto);

      transactionReadService.readRecentAmounts(null);
      transactionReadService.readRecentAmounts(now);

      verify(transactionRepository, times(2)).readRecentAmounts(anyLong(), any(LocalDateTime.class));
    }

    @Test
    void 내역_조회_단건() {
      final Schedule schedule = new Schedule(1L, "event", now, "memo", ScheduleStatus.NORMAL, now, now, null, null);
      final Person person = new Person(1L, "name", PersonRelation.ETC, "01012341234", "memo", now, now, null);
      final TransactionAmount amountDto = new TransactionAmount(TransactionAction.RECEIVING, 1000L);

      final Transaction transaction = new Transaction(1L, amountDto, "memo", now, now, TransactionCategory.ETC, person, null, schedule);

      when(transactionRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(transaction));

      final TransactionWithScheduleDto transactionWithScheduleDto = transactionReadService.readTransaction(transactionId);

      verify(transactionRepository).findByIdAndUserId(anyLong(), anyLong());
      assertThat(transactionWithScheduleDto).extracting(
              TransactionWithScheduleDto::getId,
              TransactionWithScheduleDto::getPerson,
              TransactionWithScheduleDto::getSchedule,
              TransactionWithScheduleDto::getTransactionAmount,
              TransactionWithScheduleDto::getCategory,
              TransactionWithScheduleDto::getMemo,
              TransactionWithScheduleDto::getCreatedAt,
              TransactionWithScheduleDto::getUpdatedAt
          )
          .containsExactly(
              transactionWithScheduleDto.getId(),
              transactionWithScheduleDto.getPerson(),
              transactionWithScheduleDto.getSchedule(),
              transactionWithScheduleDto.getTransactionAmount(),
              transactionWithScheduleDto.getCategory(),
              transactionWithScheduleDto.getMemo(),
              transactionWithScheduleDto.getCreatedAt(),
              transactionWithScheduleDto.getUpdatedAt()
          );
    }

    @Test
    void 내역_조회_페이지네이션() {
      final TransactionAction[] actions = TransactionAction.values();
      for (int i = 0; i < actions.length; i++) {
        final ScheduleDto schedule = new ScheduleDto(1L, "event", now, "memo", ScheduleStatus.NORMAL, now, now);
        final PersonDto person = new PersonDto(1L, "name", "01012341234", PersonRelation.ETC, "memo", now, now);
        final TransactionAmountDto amountDto = new TransactionAmountDto(TransactionAction.GIVING, 1000L);
        final TransactionWithScheduleDto transactionWithScheduleDto1 = new TransactionWithScheduleDto(1L, person, schedule, amountDto, TransactionCategory.ETC, "memo1", now, now);
        final TransactionWithScheduleDto transactionWithScheduleDto2 = new TransactionWithScheduleDto(2L, person, schedule, amountDto, TransactionCategory.ETC, "memo2", now, now);

        final Pageable pageable = PageRequest.of(0, 10);
        final TransactionPaginationParam paginationParam = new TransactionPaginationParam(pageable.getPageNumber(), pageable.getPageSize(), actions[i], null, null, null, null);
        final Page<TransactionWithScheduleDto> transactionWithScheduleDtoPage = new PageImpl<>(List.of(transactionWithScheduleDto1, transactionWithScheduleDto2), pageable, 2);

        when(transactionRepository.findAllByUserId(
            paginationParam.toPageable(), userId,
            paginationParam.getStartedAt(),
            paginationParam.getEndedAt(),
            paginationParam.getAction())
        ).thenReturn(transactionWithScheduleDtoPage);

        final TransactionPaginationResponseDto transactionPaginationResponseDto = transactionReadService.readTransactionList(paginationParam);

        verify(transactionRepository)
            .findAllByUserId(
                paginationParam.toPageable(),
                userId,
                paginationParam.getStartedAt(),
                paginationParam.getEndedAt(),
                paginationParam.getAction()
            );
        assertThat(transactionPaginationResponseDto).extracting(
                TransactionPaginationResponseDto::getTotalPages,
                TransactionPaginationResponseDto::getTotalElements,
                TransactionPaginationResponseDto::getIsFirst,
                TransactionPaginationResponseDto::getIsLast,
                TransactionPaginationResponseDto::getHasNext,
                TransactionPaginationResponseDto::getItems
            )
            .containsExactly(
                transactionPaginationResponseDto.getTotalPages(),
                transactionPaginationResponseDto.getTotalElements(),
                transactionPaginationResponseDto.getIsFirst(),
                transactionPaginationResponseDto.getIsLast(),
                transactionPaginationResponseDto.getHasNext(),
                transactionPaginationResponseDto.getItems()
            );
      }
    }
  }
}