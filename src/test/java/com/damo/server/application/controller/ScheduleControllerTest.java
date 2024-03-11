package com.damo.server.application.controller;

import com.damo.server.common.WithMockCustomUser;
import com.damo.server.domain.schedule.dto.RequestCreateScheduleDto;
import com.damo.server.domain.schedule.dto.RequestUpdateScheduleDto;
import com.damo.server.domain.schedule.entity.ScheduleStatus;
import com.damo.server.domain.schedule.service.ScheduleReadService;
import com.damo.server.domain.schedule.service.ScheduleWriteService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@WithMockCustomUser
class ScheduleControllerTest {
  private final String END_POINT = "/api/schedules";
  private final ObjectMapper mapper = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  @MockBean
  ScheduleWriteService scheduleWriteService;
  @MockBean
  ScheduleReadService scheduleReadService;
  @Autowired
  MockMvc mvc;

  @Nested
  @DisplayName("성공 케이스")
  class 성공 {
    LocalDateTime now;

    @BeforeEach
    void 초기값() {
      now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    @Test
    void 일정_추가() throws Exception {
      for (final ScheduleStatus status : ScheduleStatus.values()) {
        final RequestCreateScheduleDto createScheduleDto = new RequestCreateScheduleDto(now, "event", "memo", status, 1L);
        doNothing().when(scheduleWriteService).addSchedule(createScheduleDto);

        final String json = mapper.writeValueAsString(createScheduleDto);

        mvc.perform(post(END_POINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$").doesNotExist());
      }
    }

    @Test
    void 일정_추가_중요도_NULL() throws Exception {
      final RequestCreateScheduleDto createScheduleDto = new RequestCreateScheduleDto(now, "event", "memo", null, 1L);
      doNothing().when(scheduleWriteService).addSchedule(createScheduleDto);

      final String json = mapper.writeValueAsString(createScheduleDto);

      mvc.perform(post(END_POINT)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .accept(MediaType.APPLICATION_JSON)
          ).andDo(print())
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void 일정_추가_내역_아이디_NULL() throws Exception {
      for (final ScheduleStatus status : ScheduleStatus.values()) {
        final RequestCreateScheduleDto createScheduleDto = new RequestCreateScheduleDto(now, "event", "memo", status, null);
        doNothing().when(scheduleWriteService).addSchedule(createScheduleDto);

        final String json = mapper.writeValueAsString(createScheduleDto);

        mvc.perform(post(END_POINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$").doesNotExist());
      }
    }

    @Test
    void 일정_수정() throws Exception {
      final Long scheduleId = 1L;
      for (final ScheduleStatus status : ScheduleStatus.values()) {
        final RequestUpdateScheduleDto updateScheduleDto = new RequestUpdateScheduleDto(now, "event", "memo", status, 1L);
        doNothing().when(scheduleWriteService).patchScheduleById(updateScheduleDto, scheduleId);

        final String json = mapper.writeValueAsString(updateScheduleDto);

        mvc.perform(patch(END_POINT + "/" + scheduleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isNoContent())
            .andExpect(jsonPath("$").doesNotExist());
      }
    }

    @Test
    void 일정_수정_중요도_NULL() throws Exception {
      final Long scheduleId = 1L;
      final RequestUpdateScheduleDto updateScheduleDto = new RequestUpdateScheduleDto(now, "event", "memo", null, 1L);
      doNothing().when(scheduleWriteService).patchScheduleById(updateScheduleDto, scheduleId);

      final String json = mapper.writeValueAsString(updateScheduleDto);

      mvc.perform(patch(END_POINT + "/" + scheduleId)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              .accept(MediaType.APPLICATION_JSON)
          ).andDo(print())
          .andExpect(status().isNoContent())
          .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void 일정_수정_내역_아이디_NULL() throws Exception {
      final Long scheduleId = 1L;
      for (final ScheduleStatus status : ScheduleStatus.values()) {
        final RequestUpdateScheduleDto updateScheduleDto = new RequestUpdateScheduleDto(now, "event", "memo", status, null);
        doNothing().when(scheduleWriteService).patchScheduleById(updateScheduleDto, scheduleId);

        final String json = mapper.writeValueAsString(updateScheduleDto);

        mvc.perform(patch(END_POINT + "/" + scheduleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isNoContent())
            .andExpect(jsonPath("$").doesNotExist());
      }
    }

    @Test
    void 일정_삭제() throws Exception {
      final Long scheduleId = 1L;
      doNothing().when(scheduleWriteService).removeScheduleById(scheduleId);

      mvc.perform(delete(END_POINT + "/" + scheduleId)
          ).andDo(print())
          .andExpect(status().isNoContent())
          .andExpect(jsonPath("$").doesNotExist());
    }
  }
}

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@AllArgsConstructor
@Setter
class FailScheduleDto {
  private Object eventDate;
  private Object event;
  private Object memo;
  private Object status;
  private Object transactionId;
}
