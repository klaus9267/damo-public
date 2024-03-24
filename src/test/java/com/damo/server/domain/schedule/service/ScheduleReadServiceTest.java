package com.damo.server.domain.schedule.service;

import com.damo.server.application.security.user_details.SecurityUserUtil;
import com.damo.server.domain.common.pagination.param.SchedulePaginationParam;
import com.damo.server.domain.schedule.ScheduleRepository;
import com.damo.server.domain.schedule.dto.SchedulePaginationResponseDto;
import com.damo.server.domain.schedule.dto.ScheduleWithTransactionDto;
import com.damo.server.domain.schedule.entity.Schedule;
import com.damo.server.domain.schedule.entity.ScheduleStatus;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleReadServiceTest {
  ScheduleReadService scheduleReadService;
  @Mock
  ScheduleRepository scheduleRepository;
  @Mock
  SecurityUserUtil securityUserUtil;

  @BeforeEach
  void set_up() {
    scheduleReadService = new ScheduleReadService(scheduleRepository, securityUserUtil);
  }

  @Nested
  @DisplayName("성공 케이스")
  class 성공 {
    LocalDateTime now;
    Long userId = 1L;
    Long scheduleId = 1L;

    @BeforeEach
    void 초기값() {
      now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
      when(securityUserUtil.getId()).thenReturn(userId);
    }

    @Test
    void 일정_조회_단건() throws Exception {
      final Schedule schedule = new Schedule(1L, "event", now, "memo", ScheduleStatus.NORMAL, now, now, null, null);

      when(scheduleRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(schedule));

      scheduleReadService.readSchedule(scheduleId);

      verify(scheduleRepository).findByIdAndUserId(anyLong(), anyLong());
    }

    @Test
    void 일정_조회_페이지네이션() throws Exception {
      final Pageable pageable = PageRequest.of(0, 10);
      final ScheduleWithTransactionDto schedule1 = new ScheduleWithTransactionDto(1L, "event1", now, "memo", ScheduleStatus.NORMAL, null, now, now);
      final ScheduleWithTransactionDto schedule2 = new ScheduleWithTransactionDto(2L, "event2", now, "memo", ScheduleStatus.NORMAL, null, now, now);
      final Page<ScheduleWithTransactionDto> schedulePage = new PageImpl<>(List.of(schedule1, schedule2), pageable, 2);

      final SchedulePaginationParam paginationParam = new SchedulePaginationParam(0, 20, null, null, null, null);

      when(scheduleRepository.findAllScheduleByEventDate(paginationParam.toPageable(), userId, paginationParam.getYear(), paginationParam.getMonth(), paginationParam.getKeyword())).thenReturn(schedulePage);

      scheduleReadService.readScheduleByEventDate(paginationParam);

      verify(scheduleRepository).findAllScheduleByEventDate(paginationParam.toPageable(), userId, paginationParam.getYear(), paginationParam.getMonth(), paginationParam.getKeyword());
    }
  }
}