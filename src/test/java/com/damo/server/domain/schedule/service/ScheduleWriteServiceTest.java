package com.damo.server.domain.schedule.service;

import com.damo.server.application.security.user_details.SecurityUserUtil;
import com.damo.server.domain.schedule.ScheduleRepository;
import com.damo.server.domain.schedule.dto.RequestCreateScheduleDto;
import com.damo.server.domain.schedule.entity.Schedule;
import com.damo.server.domain.schedule.entity.ScheduleStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ScheduleWriteServiceTest {
  @Mock
  SecurityUserUtil securityUserUtil;
  @Mock
  ScheduleRepository scheduleRepository;
  @InjectMocks
  ScheduleWriteService scheduleWriteService;

  @Nested
  @DisplayName("성공 케이스")
  class 성공 {
    LocalDateTime now;

    @BeforeEach
    void 초기값() {
      now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    @Test
    void 일정_생성() throws Exception {
      final RequestCreateScheduleDto scheduleDto = new RequestCreateScheduleDto(now, "event", "memo", ScheduleStatus.NORMAL, null);
      final Long userId = 1L;
      final Schedule schedule = Schedule.from(scheduleDto, userId);

      given(securityUserUtil.getId()).willReturn(userId);
      given(scheduleRepository.findByEventAndEventDateAndUserId(anyString(), any(), anyLong())).willReturn(Optional.empty());
      given(scheduleRepository.save(any(Schedule.class))).willReturn(schedule);

      scheduleWriteService.addSchedule(scheduleDto);

      verify(scheduleRepository).findByEventAndEventDateAndUserId(scheduleDto.event(), scheduleDto.eventDate(), userId);
      verify(scheduleRepository).save(any(Schedule.class));
    }
  }
}