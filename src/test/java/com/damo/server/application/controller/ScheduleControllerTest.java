package com.damo.server.application.controller;

import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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

  @Nested
  @DisplayName("실패 케이스")
  class 실패 {
    @Nested
    @DisplayName("일정_생성_실패")
    class 일정_생성_실패 {
      LocalDateTime now;
      FailScheduleDto failScheduleDto;

      @BeforeEach
      void 초기값() {
        now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        failScheduleDto = new FailScheduleDto(now, "event", "memo", ScheduleStatus.IMPORTANT, 1L);
      }

      private void callApiForBadRequestWhenCreate() throws Exception {
        final String json = mapper.writeValueAsString(failScheduleDto);

        mvc.perform(post(END_POINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
            ).andDo(print())
            .andExpect(status().isBadRequest());
      }

      @Test
      void 일정_거래_날짜_NULL() throws Exception {
        failScheduleDto.setEventDate(null);
        callApiForBadRequestWhenCreate();
      }

      @Test
      void 잘못된_일정_거래_날짜_문자열() throws Exception {
        failScheduleDto.setEventDate("열흘 뒤");
        callApiForBadRequestWhenCreate();
      }

      @Test
      void 잘못된_일정_거래_날짜_정수() throws Exception {
        failScheduleDto.setEventDate(123);
        callApiForBadRequestWhenCreate();
      }

      @Test
      void 잘못된_일정_거래_날짜_음수() throws Exception {
        failScheduleDto.setEventDate(-123);
        callApiForBadRequestWhenCreate();
      }

      @Test
      void 잘못된_일정_거래_날짜_소수() throws Exception {
        failScheduleDto.setEventDate(1.23);
        callApiForBadRequestWhenCreate();
      }

      @Test
      void 일정_행사_이름_NULL() throws Exception {
        failScheduleDto.setEvent(null);
        callApiForBadRequestWhenCreate();
      }

      @Test
      void 일정_행사_이름_빈문자열() throws Exception {
        failScheduleDto.setEvent("");
        callApiForBadRequestWhenCreate();
      }

      @Test
      void 일정_행사_이름_공백() throws Exception {
        failScheduleDto.setEvent(" ");
        callApiForBadRequestWhenCreate();
      }

      @Test
      void 일정_메모_200자_초과() throws Exception {
        failScheduleDto.setMemo("m".repeat(201));
        callApiForBadRequestWhenCreate();
      }

      @Test
      void 잘못된_일정_중요도() throws Exception {
        failScheduleDto.setStatus("그렇게 중요하진 않은데 안나가기 애매한 일정");
        callApiForBadRequestWhenCreate();
      }

      @Test
      void 잘못된_일정_내역_아이디() throws Exception {
        failScheduleDto.setTransactionId("일번");
        callApiForBadRequestWhenCreate();
      }
    }

    @Nested
    @DisplayName("일정_수정_실패")
    class 일정_수정_실패 {
      LocalDateTime now;
      FailScheduleDto failScheduleDto;

      @BeforeEach
      void 초기값() {
        now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        failScheduleDto = new FailScheduleDto(now, "event", "memo", ScheduleStatus.IMPORTANT, 1L);
      }

      private void callApiForBadRequestWhenUpdate() throws Exception {
        final Long scheduleId = 1L;
        final String json = mapper.writeValueAsString(failScheduleDto);

        mvc.perform(patch(END_POINT + "/" + scheduleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
            ).andDo(print())
            .andExpect(status().isBadRequest());
      }

      @Test
      void 잘못된_일정_거래_날짜_문자열() throws Exception {
        failScheduleDto.setEventDate("열흘 뒤");
        callApiForBadRequestWhenUpdate();
      }

      @Test
      void 잘못된_일정_거래_날짜_정수() throws Exception {
        failScheduleDto.setEventDate(123);
        callApiForBadRequestWhenUpdate();
      }

      @Test
      void 잘못된_일정_거래_날짜_음수() throws Exception {
        failScheduleDto.setEventDate(-123);
        callApiForBadRequestWhenUpdate();
      }

      @Test
      void 잘못된_일정_거래_날짜_소수() throws Exception {
        failScheduleDto.setEventDate(1.23);
        callApiForBadRequestWhenUpdate();
      }

      @Test
      void 일정_행사_이름_NULL() throws Exception {
        failScheduleDto.setEvent(null);
        callApiForBadRequestWhenUpdate();
      }

      @Test
      void 일정_행사_이름_빈문자열() throws Exception {
        failScheduleDto.setEvent("");
        callApiForBadRequestWhenUpdate();
      }

      @Test
      void 일정_행사_이름_공백() throws Exception {
        failScheduleDto.setEvent(" ");
        callApiForBadRequestWhenUpdate();
      }

      @Test
      void 일정_메모_200자_초과() throws Exception {
        failScheduleDto.setMemo("m".repeat(201));
        callApiForBadRequestWhenUpdate();
      }

      @Test
      void 잘못된_일정_중요도() throws Exception {
        failScheduleDto.setStatus("그렇게 중요하진 않은데 안나가기 애매한 일정");
        callApiForBadRequestWhenUpdate();
      }

      @Test
      void 잘못된_일정_내역_아이디() throws Exception {
        failScheduleDto.setTransactionId("일번");
        callApiForBadRequestWhenUpdate();
      }
    }

    @Nested
    @DisplayName("일정_삭제_실패")
    class 일정_삭제_실패 {
      @Test
      void 존재하지_않는_일정() throws Exception {
        doThrow(new CustomException(CustomErrorCode.NOT_FOUND, "삭제할 일정을 찾을 수 없음")).when(scheduleWriteService).removeScheduleById(anyLong());

        final Long scheduleId = 1L;

        mvc.perform(delete(END_POINT + "/" + scheduleId))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("삭제할 일정을 찾을 수 없음"));
      }

      @Test
      void 잘못된_일정_아이디_문자열() throws Exception {
        mvc.perform(delete(END_POINT + "/1번"))
            .andDo(print())
            .andExpect(status().isBadRequest());
      }

      @Test
      void 잘못된_일정_아이디_소수() throws Exception {
        mvc.perform(delete(END_POINT + "/0.3"))
            .andDo(print())
            .andExpect(status().isBadRequest());
      }
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
