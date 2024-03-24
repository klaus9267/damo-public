package com.damo.server.domain.schedule.service;

import com.damo.server.application.handler.exception.CustomException;
import com.damo.server.application.security.user_details.SecurityUserUtil;
import com.damo.server.domain.schedule.ScheduleRepository;
import com.damo.server.domain.schedule.dto.RequestCreateScheduleDto;
import com.damo.server.domain.schedule.dto.RequestUpdateScheduleDto;
import com.damo.server.domain.schedule.entity.Schedule;
import com.damo.server.domain.schedule.entity.ScheduleStatus;
import jakarta.transaction.Transactional;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class ScheduleWriteServiceTest {
  ScheduleWriteService scheduleWriteService;
  @Mock
  ScheduleRepository scheduleRepository;
  @Mock
  SecurityUserUtil securityUserUtil;

  @BeforeEach
  void set_up() {
    scheduleWriteService = new ScheduleWriteService(scheduleRepository, securityUserUtil);
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
    void 일정_생성_중요도() throws Exception {
      for (final ScheduleStatus status : ScheduleStatus.values()) {
        final RequestCreateScheduleDto scheduleDto = new RequestCreateScheduleDto(now, status.getKey(), "memo", status, 1L);
        final Schedule schedule = Schedule.from(scheduleDto, userId);

        when(scheduleRepository.findByEventAndEventDateAndUserId(scheduleDto.event(), scheduleDto.eventDate(), userId)).thenReturn(Optional.empty());
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        scheduleWriteService.addSchedule(scheduleDto);
      }

      verify(scheduleRepository, times(ScheduleStatus.values().length)).findByEventAndEventDateAndUserId(anyString(), any(), anyLong());
      verify(scheduleRepository, times(ScheduleStatus.values().length)).save(any(Schedule.class));
    }

    @Test
    void 일정_수정_중요도() throws Exception {
      for (final ScheduleStatus status : ScheduleStatus.values()) {
        final RequestUpdateScheduleDto scheduleDto = new RequestUpdateScheduleDto(now, "event", "memo", status, null);
        final Schedule schedule = new Schedule(1L, scheduleDto.event(), scheduleDto.eventDate(), scheduleDto.memo(), scheduleDto.status(), now, now, null, null);

        when(securityUserUtil.getId()).thenReturn(userId);
        when(scheduleRepository.findByIdAndUserId(scheduleId, userId)).thenReturn(Optional.of(schedule));

        scheduleWriteService.patchScheduleById(scheduleDto, scheduleId);
      }
      verify(scheduleRepository, times(ScheduleStatus.values().length)).findByIdAndUserId(scheduleId, userId);
      verify(scheduleRepository, never()).save(any(Schedule.class));
    }

    @Test
    void 일정_삭제() throws Exception {
      final Schedule schedule = new Schedule(1L, "event", now, "memo", ScheduleStatus.NORMAL, now, now, null, null);

      when(scheduleRepository.findByIdAndUserId(scheduleId, userId)).thenReturn(Optional.of(schedule));
      doNothing().when(scheduleRepository).deleteById(scheduleId);

      scheduleWriteService.removeScheduleById(scheduleId);

      verify(scheduleRepository).deleteById(scheduleId);
    }
  }
}