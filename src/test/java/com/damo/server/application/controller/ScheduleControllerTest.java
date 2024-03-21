package com.damo.server.application.controller;

import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import com.damo.server.common.PaginationTestParameter;
import com.damo.server.common.WithMockCustomUser;
import com.damo.server.domain.common.pagination.param.SchedulePaginationParam;
import com.damo.server.domain.person.dto.PersonDto;
import com.damo.server.domain.person.entity.PersonRelation;
import com.damo.server.domain.schedule.dto.*;
import com.damo.server.domain.schedule.entity.ScheduleStatus;
import com.damo.server.domain.schedule.service.ScheduleReadService;
import com.damo.server.domain.schedule.service.ScheduleWriteService;
import com.damo.server.domain.transaction.dto.TransactionAmountDto;
import com.damo.server.domain.transaction.dto.TransactionDto;
import com.damo.server.domain.transaction.entity.TransactionAction;
import com.damo.server.domain.transaction.entity.TransactionCategory;
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
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
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

    @Test
    void 일정_조회_단건() throws Exception {
      final PersonDto personDto = new PersonDto(1L, "name", "01012341234", PersonRelation.ETC, "memo,now", now, now);
      final ScheduleDto scheduleDto = new ScheduleDto(1L, "event,now", now, "memo", ScheduleStatus.IMPORTANT, now, now);
      final TransactionAmountDto transactionAmountDto = new TransactionAmountDto(TransactionAction.RECEIVING, 1000L);
      final TransactionDto transactionDto = new TransactionDto(1L, personDto, transactionAmountDto, TransactionCategory.ETC, "memo", now, now);

      final ScheduleWithTransactionDto scheduleWithTransactionDto = new ScheduleWithTransactionDto(1L, "event", now, "memo", ScheduleStatus.NORMAL, transactionDto, now, now);

      when(scheduleReadService.readSchedule(scheduleWithTransactionDto.getId())).thenReturn(scheduleWithTransactionDto);

      mvc.perform(get(END_POINT + "/" + scheduleWithTransactionDto.getId())
              .contentType(MediaType.APPLICATION_JSON)
          ).andDo(print())
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(scheduleWithTransactionDto.getId()))
          .andExpect(jsonPath("$.event").value(scheduleWithTransactionDto.getEvent()))
          .andExpect(jsonPath("$.eventDate").value(scheduleWithTransactionDto.getEventDate().toString()))
          .andExpect(jsonPath("$.memo").value(scheduleWithTransactionDto.getMemo()))
          .andExpect(jsonPath("$.status").value(scheduleWithTransactionDto.getStatus().getKey()))
          .andExpect(jsonPath("$.createdAt").value(scheduleWithTransactionDto.getCreatedAt().toString()))
          .andExpect(jsonPath("$.updatedAt").value(scheduleWithTransactionDto.getUpdatedAt().toString()))

          .andExpect(jsonPath("$.transaction.id").value(scheduleWithTransactionDto.getTransaction().getId()))
          .andExpect(jsonPath("$.transaction.category").value(scheduleWithTransactionDto.getTransaction().getCategory().getKey()))
          .andExpect(jsonPath("$.transaction.memo").value(scheduleWithTransactionDto.getTransaction().getMemo()))
          .andExpect(jsonPath("$.transaction.createdAt").value(scheduleWithTransactionDto.getTransaction().getCreatedAt().toString()))
          .andExpect(jsonPath("$.transaction.updatedAt").value(scheduleWithTransactionDto.getTransaction().getUpdatedAt().toString()))

          .andExpect(jsonPath("$.transaction.transactionAmount.action").value(scheduleWithTransactionDto.getTransaction().getTransactionAmount().action().getKey()))
          .andExpect(jsonPath("$.transaction.transactionAmount.amount").value(scheduleWithTransactionDto.getTransaction().getTransactionAmount().amount()))

          .andExpect(jsonPath("$.transaction.person.id").value(scheduleWithTransactionDto.getTransaction().getPerson().getId()))
          .andExpect(jsonPath("$.transaction.person.name").value(scheduleWithTransactionDto.getTransaction().getPerson().getName()))
          .andExpect(jsonPath("$.transaction.person.contact").value(scheduleWithTransactionDto.getTransaction().getPerson().getContact()))
          .andExpect(jsonPath("$.transaction.person.relation").value(scheduleWithTransactionDto.getTransaction().getPerson().getRelation().getKey()))
          .andExpect(jsonPath("$.transaction.person.createdAt").value(scheduleWithTransactionDto.getTransaction().getPerson().getCreatedAt().toString()))
          .andExpect(jsonPath("$.transaction.person.updatedAt").value(scheduleWithTransactionDto.getTransaction().getPerson().getUpdatedAt().toString()));
    }

    @Test
    void 일정_목록_조회() throws Exception {
      final PersonDto personDto = new PersonDto(1L, "name", "01012341234", PersonRelation.ETC, "memo,now", now, now);
      final ScheduleDto scheduleDto = new ScheduleDto(1L, "event,now", now, "memo", ScheduleStatus.IMPORTANT, now, now);
      final TransactionAmountDto transactionAmountDto = new TransactionAmountDto(TransactionAction.RECEIVING, 1000L);
      final TransactionDto transactionDto = new TransactionDto(1L, personDto, transactionAmountDto, TransactionCategory.ETC, "memo", now, now);
      final List<ScheduleWithTransactionDto> scheduleWithTransactionDtoList = List.of(new ScheduleWithTransactionDto(1L, "event", now, "memo", ScheduleStatus.NORMAL, transactionDto, now, now));
      final SchedulePaginationResponseDto schedulePaginationResponseDto = new SchedulePaginationResponseDto(1, 1L, true, true, false, scheduleWithTransactionDtoList);

      final MultiValueMap<String, String> parameters = PaginationTestParameter.getInitialParams();
      parameters.add("date", String.valueOf(LocalDate.now()));

      when(scheduleReadService.readScheduleByEventDate(any(SchedulePaginationParam.class))).thenReturn(schedulePaginationResponseDto);

      mvc.perform(get(END_POINT).params(parameters)
              .contentType(MediaType.APPLICATION_JSON)
          ).andDo(print())
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.totalPages").value(schedulePaginationResponseDto.getTotalPages()))
          .andExpect(jsonPath("$.totalElements").value(schedulePaginationResponseDto.getTotalElements()))
          .andExpect(jsonPath("$.isFirst").value(schedulePaginationResponseDto.getIsFirst()))
          .andExpect(jsonPath("$.isLast").value(schedulePaginationResponseDto.getIsLast()))
          .andExpect(jsonPath("$.hasNext").value(schedulePaginationResponseDto.getHasNext()))

          .andExpect(jsonPath("$.items[0].id").value(scheduleWithTransactionDtoList.get(0).getId()))
          .andExpect(jsonPath("$.items[0].event").value(scheduleWithTransactionDtoList.get(0).getEvent()))
          .andExpect(jsonPath("$.items[0].eventDate").value(scheduleWithTransactionDtoList.get(0).getEventDate().toString()))
          .andExpect(jsonPath("$.items[0].memo").value(scheduleWithTransactionDtoList.get(0).getMemo()))
          .andExpect(jsonPath("$.items[0].status").value(scheduleWithTransactionDtoList.get(0).getStatus().getKey()))
          .andExpect(jsonPath("$.items[0].createdAt").value(scheduleWithTransactionDtoList.get(0).getCreatedAt().toString()))
          .andExpect(jsonPath("$.items[0].updatedAt").value(scheduleWithTransactionDtoList.get(0).getUpdatedAt().toString()))

          .andExpect(jsonPath("$.items[0].transaction.id").value(scheduleWithTransactionDtoList.get(0).getTransaction().getId()))
          .andExpect(jsonPath("$.items[0].transaction.category").value(scheduleWithTransactionDtoList.get(0).getTransaction().getCategory().getKey()))
          .andExpect(jsonPath("$.items[0].transaction.memo").value(scheduleWithTransactionDtoList.get(0).getTransaction().getMemo()))
          .andExpect(jsonPath("$.items[0].transaction.createdAt").value(scheduleWithTransactionDtoList.get(0).getTransaction().getCreatedAt().toString()))
          .andExpect(jsonPath("$.items[0].transaction.updatedAt").value(scheduleWithTransactionDtoList.get(0).getTransaction().getUpdatedAt().toString()))

          .andExpect(jsonPath("$.items[0].transaction.transactionAmount.action").value(scheduleWithTransactionDtoList.get(0).getTransaction().getTransactionAmount().action().getKey()))
          .andExpect(jsonPath("$.items[0].transaction.transactionAmount.amount").value(scheduleWithTransactionDtoList.get(0).getTransaction().getTransactionAmount().amount()))

          .andExpect(jsonPath("$.items[0].transaction.person.id").value(scheduleWithTransactionDtoList.get(0).getTransaction().getPerson().getId()))
          .andExpect(jsonPath("$.items[0].transaction.person.name").value(scheduleWithTransactionDtoList.get(0).getTransaction().getPerson().getName()))
          .andExpect(jsonPath("$.items[0].transaction.person.contact").value(scheduleWithTransactionDtoList.get(0).getTransaction().getPerson().getContact()))
          .andExpect(jsonPath("$.items[0].transaction.person.relation").value(scheduleWithTransactionDtoList.get(0).getTransaction().getPerson().getRelation().getKey()))
          .andExpect(jsonPath("$.items[0].transaction.person.createdAt").value(scheduleWithTransactionDtoList.get(0).getTransaction().getPerson().getCreatedAt().toString()))
          .andExpect(jsonPath("$.items[0].transaction.person.updatedAt").value(scheduleWithTransactionDtoList.get(0).getTransaction().getPerson().getUpdatedAt().toString()));
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
