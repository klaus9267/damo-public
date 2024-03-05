package com.damo.server.application.controller;

import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import com.damo.server.common.WithMockCustomUser;
import com.damo.server.domain.common.pagination.param.TransactionPaginationParam;
import com.damo.server.domain.person.dto.PersonDto;
import com.damo.server.domain.person.entity.PersonRelation;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import com.damo.server.domain.schedule.entity.ScheduleStatus;
import com.damo.server.domain.transaction.dto.*;
import com.damo.server.domain.transaction.entity.TransactionAction;
import com.damo.server.domain.transaction.entity.TransactionAmount;
import com.damo.server.domain.transaction.entity.TransactionCategory;
import com.damo.server.domain.transaction.service.TransactionReadService;
import com.damo.server.domain.transaction.service.TransactionWriteService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockCustomUser
@Transactional
class TransactionControllerTest {
  final ObjectMapper mapper = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  private final String END_POINT = "/api/transactions";

  @Autowired
  MockMvc mvc;

  @MockBean
  TransactionWriteService transactionWriteService;
  @MockBean
  TransactionReadService transactionReadService;

  @Nested
  @DisplayName("성공 케이스")
  class 성공 {
    final TransactionAction action = TransactionAction.GIVING;
    final TransactionCategory category = TransactionCategory.ETC;
    final Long transactionId = 1L;

    @Test
    void 내역_삭제() throws Exception {
      doNothing().when(transactionWriteService).removeTransactionById(transactionId);

      mvc.perform(delete(END_POINT + "/" + transactionId))
          .andDo(print())
          .andExpect(status().isNoContent())
          .andExpect(jsonPath("$").doesNotExist());
    }

    @Nested
    @DisplayName("내역_조회_성공")
    class 내역_조회 {
      private LocalDateTime now;

      @BeforeEach
      void 초기값() {
        now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
      }

      @Test
      void 내역_조회_최근_거래_총액() throws Exception {
        final TransactionAmount receivingAmount = new TransactionAmount(TransactionAction.RECEIVING, 1000L);
        final TransactionAmount givingAmount = new TransactionAmount(TransactionAction.GIVING, 2000L);
        final TransactionTotalAmountDto totalAmountDto = new TransactionTotalAmountDto(givingAmount, receivingAmount);

        when(transactionReadService.readRecentAmounts(now)).thenReturn(totalAmountDto);

        mvc.perform(get(END_POINT + "/term-amounts")
                .param("startedAt", String.valueOf(now))
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalGivingAmount").value(totalAmountDto.getTotalGivingAmount()))
            .andExpect(jsonPath("$.totalReceivingAmount").value(totalAmountDto.getTotalReceivingAmount()));
      }

      @Test
      void 내역_조회_전체_거래_총액() throws Exception {
        final TransactionAmount receivingAmount = new TransactionAmount(TransactionAction.RECEIVING, 1000L);
        final TransactionAmount givingAmount = new TransactionAmount(TransactionAction.GIVING, 2000L);
        final TransactionTotalAmountDto totalAmountDto = new TransactionTotalAmountDto(givingAmount, receivingAmount);

        when(transactionReadService.readTotalAmounts()).thenReturn(totalAmountDto);

        mvc.perform(get(END_POINT + "/total-amounts")
                .param("startedAt", String.valueOf(now))
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalGivingAmount").value(totalAmountDto.getTotalGivingAmount()))
            .andExpect(jsonPath("$.totalReceivingAmount").value(totalAmountDto.getTotalReceivingAmount()));
      }

      @Test
      void 내역_조회_단건() throws Exception {
        final PersonDto personDto = new PersonDto(1L, "name", "01012341234", PersonRelation.ETC, "memo,now", now, now);
        final ScheduleDto scheduleDto = new ScheduleDto(1L, "event,now", now, "memo", ScheduleStatus.IMPORTANT, now, now);
        final TransactionAmountDto transactionAmountDto = new TransactionAmountDto(TransactionAction.RECEIVING, 1000L);

        final TransactionWithScheduleDto transactionWithScheduleDto = new TransactionWithScheduleDto(1L, personDto, scheduleDto, transactionAmountDto, TransactionCategory.ETC, "memo", now, now);

        when(transactionReadService.readTransaction(transactionWithScheduleDto.getId())).thenReturn(transactionWithScheduleDto);

        mvc.perform(get(END_POINT + "/" + transactionWithScheduleDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(transactionWithScheduleDto.getId()))
            .andExpect(jsonPath("$.transactionAmount.amount").value(transactionWithScheduleDto.getTransactionAmount().amount()))
            .andExpect(jsonPath("$.category").value(transactionWithScheduleDto.getCategory().getKey()))
            .andExpect(jsonPath("$.memo").value(transactionWithScheduleDto.getMemo()))
            .andExpect(jsonPath("$.createdAt").value(transactionWithScheduleDto.getCreatedAt().toString()))
            .andExpect(jsonPath("$.updatedAt").value(transactionWithScheduleDto.getUpdatedAt().toString()))

            .andExpect(jsonPath("$.person.id").value(transactionWithScheduleDto.getPerson().getId()))
            .andExpect(jsonPath("$.person.name").value(transactionWithScheduleDto.getPerson().getName()))
            .andExpect(jsonPath("$.person.contact").value(transactionWithScheduleDto.getPerson().getContact()))
            .andExpect(jsonPath("$.person.relation").value(transactionWithScheduleDto.getPerson().getRelation().getKey()))
            .andExpect(jsonPath("$.person.createdAt").value(transactionWithScheduleDto.getPerson().getCreatedAt().toString()))
            .andExpect(jsonPath("$.person.updatedAt").value(transactionWithScheduleDto.getPerson().getUpdatedAt().toString()))

            .andExpect(jsonPath("$.schedule.id").value(transactionWithScheduleDto.getSchedule().getId()))
            .andExpect(jsonPath("$.schedule.event").value(transactionWithScheduleDto.getSchedule().getEvent()))
            .andExpect(jsonPath("$.schedule.eventDate").value(transactionWithScheduleDto.getSchedule().getEventDate().toString()))
            .andExpect(jsonPath("$.schedule.memo").value(transactionWithScheduleDto.getSchedule().getMemo()))
            .andExpect(jsonPath("$.schedule.status").value(transactionWithScheduleDto.getSchedule().getStatus().getKey()))
            .andExpect(jsonPath("$.schedule.createdAt").value(transactionWithScheduleDto.getSchedule().getCreatedAt().toString()))
            .andExpect(jsonPath("$.schedule.updatedAt").value(transactionWithScheduleDto.getSchedule().getUpdatedAt().toString()));
      }

      @Test
      void 내역_목록_조회_거래_종류() throws Exception {
        final PersonDto personDto = new PersonDto(1L, "name", "01012341234", PersonRelation.ETC, "memo,now", now, now);
        final ScheduleDto scheduleDto = new ScheduleDto(1L, "event,now", now, "memo", ScheduleStatus.IMPORTANT, now, now);

        for (final TransactionAction transactionAction : TransactionAction.values()) {
          final TransactionAmountDto transactionAmountDto = new TransactionAmountDto(transactionAction, 1000L);
          final List<TransactionWithScheduleDto> transactionWithScheduleDtoList = List.of(new TransactionWithScheduleDto(1L, personDto, scheduleDto, transactionAmountDto, TransactionCategory.ETC, "memo", now, now));
          final TransactionPaginationResponseDto paginationResponseDto = new TransactionPaginationResponseDto(1, 1L, true, true, false, transactionWithScheduleDtoList);

          final MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
          parameters.put("page", List.of("0"));
          parameters.put("size", List.of("0"));
          parameters.put("action", List.of(transactionAction.getKey()));

          when(transactionReadService.readTransactionList(any(TransactionPaginationParam.class))).thenReturn(paginationResponseDto);

          mvc.perform(get(END_POINT).params(parameters)
                  .contentType(MediaType.APPLICATION_JSON)
              ).andDo(print())
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.totalPages").value(paginationResponseDto.getTotalPages()))
              .andExpect(jsonPath("$.totalElements").value(paginationResponseDto.getTotalElements()))
              .andExpect(jsonPath("$.isFirst").value(paginationResponseDto.getIsFirst()))
              .andExpect(jsonPath("$.isLast").value(paginationResponseDto.getIsLast()))
              .andExpect(jsonPath("$.hasNext").value(paginationResponseDto.getHasNext()))

              .andExpect(jsonPath("$.items[0].id").value(transactionWithScheduleDtoList.get(0).getId()))
              .andExpect(jsonPath("$.items[0].memo").value(transactionWithScheduleDtoList.get(0).getMemo()))
              .andExpect(jsonPath("$.items[0].createdAt").value(transactionWithScheduleDtoList.get(0).getCreatedAt().toString()))
              .andExpect(jsonPath("$.items[0].updatedAt").value(transactionWithScheduleDtoList.get(0).getUpdatedAt().toString()))
              .andExpect(jsonPath("$.items[0].category").value(transactionWithScheduleDtoList.get(0).getCategory().getKey()))

              .andExpect(jsonPath("$.items[0].transactionAmount.action").value(transactionWithScheduleDtoList.get(0).getTransactionAmount().action().getKey()))
              .andExpect(jsonPath("$.items[0].transactionAmount.amount").value(transactionWithScheduleDtoList.get(0).getTransactionAmount().amount()))

              .andExpect(jsonPath("$.items[0].person.id").value(transactionWithScheduleDtoList.get(0).getPerson().getId()))
              .andExpect(jsonPath("$.items[0].person.name").value(transactionWithScheduleDtoList.get(0).getPerson().getName()))
              .andExpect(jsonPath("$.items[0].person.contact").value(transactionWithScheduleDtoList.get(0).getPerson().getContact()))
              .andExpect(jsonPath("$.items[0].person.relation").value(transactionWithScheduleDtoList.get(0).getPerson().getRelation().getKey()))
              .andExpect(jsonPath("$.items[0].person.createdAt").value(transactionWithScheduleDtoList.get(0).getPerson().getCreatedAt().toString()))
              .andExpect(jsonPath("$.items[0].person.updatedAt").value(transactionWithScheduleDtoList.get(0).getPerson().getUpdatedAt().toString()))

              .andExpect(jsonPath("$.items[0].schedule.id").value(transactionWithScheduleDtoList.get(0).getSchedule().getId()))
              .andExpect(jsonPath("$.items[0].schedule.event").value(transactionWithScheduleDtoList.get(0).getSchedule().getEvent()))
              .andExpect(jsonPath("$.items[0].schedule.eventDate").value(transactionWithScheduleDtoList.get(0).getSchedule().getEventDate().toString()))
              .andExpect(jsonPath("$.items[0].schedule.memo").value(transactionWithScheduleDtoList.get(0).getSchedule().getMemo()))
              .andExpect(jsonPath("$.items[0].schedule.status").value(transactionWithScheduleDtoList.get(0).getSchedule().getStatus().getKey()))
              .andExpect(jsonPath("$.items[0].schedule.createdAt").value(transactionWithScheduleDtoList.get(0).getSchedule().getCreatedAt().toString()))
              .andExpect(jsonPath("$.items[0].schedule.updatedAt").value(transactionWithScheduleDtoList.get(0).getSchedule().getUpdatedAt().toString()));
        }
      }
    }

    @Nested
    @DisplayName("내역_수정_성공")
    class 내역_수정 {
      @Test
      void 내역_수정_거래_종류() throws Exception {
        for (final TransactionAction transactionAction : TransactionAction.values()) {
          if (TransactionAction.TOTAL.equals(transactionAction)) {
            continue;
          }
          final TransactionAmountDto transactionAmount = new TransactionAmountDto(transactionAction, 1000L);
          final RequestUpdateTransactionDto updateTransactionDto = new RequestUpdateTransactionDto(1L, transactionAmount, category, "테스트 메모");

          doNothing().when(transactionWriteService).patchTransactionById(updateTransactionDto, transactionId);

          final String json = mapper.writeValueAsString(updateTransactionDto);

          mvc.perform(patch(END_POINT + "/" + transactionId)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(json)
                  .accept(MediaType.APPLICATION_JSON)
              ).andDo(print())
              .andExpect(status().isNoContent())
              .andExpect(jsonPath("$").doesNotExist());
        }
      }

      @Test
      void 내역_수정_거래_자산_유형() throws Exception {
        final TransactionAmountDto transactionAmount = new TransactionAmountDto(action, 1000L);
        for (final TransactionCategory transactionCategory : TransactionCategory.values()) {
          final RequestUpdateTransactionDto updateTransactionDto = new RequestUpdateTransactionDto(1L, transactionAmount, transactionCategory, "테스트 메모");

          doNothing().when(transactionWriteService).patchTransactionById(updateTransactionDto, transactionId);

          final String json = mapper.writeValueAsString(updateTransactionDto);

          mvc.perform(patch(END_POINT + "/" + transactionId)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(json)
                  .accept(MediaType.APPLICATION_JSON)
              ).andDo(print())
              .andExpect(status().isNoContent())
              .andExpect(jsonPath("$").doesNotExist());
        }
      }
    }

    @Nested
    @DisplayName("내역_생성_성공")
    class 내역_생성 {
      LocalDateTime now;

      @BeforeEach
      void 초기값() {
        now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
      }

      @Test
      void 내역_생성_거래_종류() throws Exception {
        for (final TransactionAction transactionAction : TransactionAction.values()) {
          if (TransactionAction.TOTAL.equals(transactionAction)) {
            continue;
          }
          final TransactionAmountDto transactionAmount = new TransactionAmountDto(transactionAction, 1000L);
          final RequestCreateTransactionDto createTransactionDto = new RequestCreateTransactionDto(1L, now, "테스트 이벤트", transactionAmount, category, "테스트 메모");

          doNothing().when(transactionWriteService).save(createTransactionDto);

          final String json = mapper.writeValueAsString(createTransactionDto);

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
      void 내역_생성_거래_자산_유형() throws Exception {
        for (final TransactionCategory transactionCategory : TransactionCategory.values()) {
          final TransactionAmountDto transactionAmount = new TransactionAmountDto(action, 1000L);
          final RequestCreateTransactionDto createTransactionDto = new RequestCreateTransactionDto(1L, now, "테스트 이벤트", transactionAmount, transactionCategory, "테스트 메모");
          doNothing().when(transactionWriteService).save(createTransactionDto);

          final String json = mapper.writeValueAsString(createTransactionDto);

          mvc.perform(post(END_POINT)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(json)
                  .accept(MediaType.APPLICATION_JSON)
              ).andDo(print())
              .andExpect(status().isCreated())
              .andExpect(jsonPath("$").doesNotExist());
        }
      }
    }
  }

  @Nested
  @DisplayName("실패 케이스")
  class 실패 {
    TransactionAction action;
    TransactionCategory category;
    FailTransactionAmountDto transactionAmount;

    @Nested
    @DisplayName("내역_생성_실패")
    class 내역_생성_실패 {
      LocalDateTime now;

      @BeforeEach
      void 초기값() {
        now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        transactionAmount = new FailTransactionAmountDto(action, 1000L);
        action = TransactionAction.RECEIVING;
        category = TransactionCategory.ETC;
      }

      private void callApiForBadRequestWhenCreate(final FailTransactionWithScheduleDto dto) throws Exception {
        final String json = mapper.writeValueAsString(dto);

        mvc.perform(post(END_POINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
            ).andDo(print())
            .andExpect(status().isBadRequest());
      }

      @Test
      void 내역_대상_아이디_NULL() throws Exception {
        final FailTransactionWithScheduleDto dto = new FailTransactionWithScheduleDto(null, now, "event", transactionAmount, category, "memo");
        callApiForBadRequestWhenCreate(dto);
      }

      @Test
      void 내역_날짜_NULL() throws Exception {
        final FailTransactionWithScheduleDto dto = new FailTransactionWithScheduleDto(1L, null, "event", transactionAmount, category, "memo");
        callApiForBadRequestWhenCreate(dto);
      }

      @Test
      void 내역_행사_이름_NULL() throws Exception {
        final FailTransactionWithScheduleDto dto = new FailTransactionWithScheduleDto(1L, now, null, transactionAmount, category, "memo");
        callApiForBadRequestWhenCreate(dto);
      }

      @Test
      void 내역_금액_객체_NULL() throws Exception {
        final FailTransactionWithScheduleDto dto = new FailTransactionWithScheduleDto(1L, now, "event", null, category, "memo");
        callApiForBadRequestWhenCreate(dto);
      }

      @Test
      void 내역_거래_종류_NULL() throws Exception {
        transactionAmount = new FailTransactionAmountDto(null, 1000L);
        final FailTransactionWithScheduleDto dto = new FailTransactionWithScheduleDto(1L, now, "event", transactionAmount, category, "memo");
        callApiForBadRequestWhenCreate(dto);
      }

      @Test
      void 내역_거래_금액_NULL() throws Exception {
        transactionAmount = new FailTransactionAmountDto(action, null);
        final FailTransactionWithScheduleDto dto = new FailTransactionWithScheduleDto(1L, now, "event", transactionAmount, category, "memo");
        callApiForBadRequestWhenCreate(dto);
      }

      @Test
      void 내역_거래_자산_종류_NULL() throws Exception {
        final FailTransactionWithScheduleDto dto = new FailTransactionWithScheduleDto(1L, now, "event", transactionAmount, null, "memo");
        callApiForBadRequestWhenCreate(dto);
      }

      @Test
      void 내역_행사_이름_빈문자열() throws Exception {
        final FailTransactionWithScheduleDto dto = new FailTransactionWithScheduleDto(1L, now, "", transactionAmount, category, "memo");
        callApiForBadRequestWhenCreate(dto);
      }

      @Test
      void 내역_행사_이름_공백() throws Exception {
        final FailTransactionWithScheduleDto dto = new FailTransactionWithScheduleDto(1L, now, " ", transactionAmount, category, "memo");
        callApiForBadRequestWhenCreate(dto);
      }

      @Test
      void 내역_메모_200자_초과() throws Exception {
        final FailTransactionWithScheduleDto dto = new FailTransactionWithScheduleDto(1L, now, "event", transactionAmount, category, "m".repeat(201));
        callApiForBadRequestWhenCreate(dto);
      }

      @Test
      void 내역_선물_종류_허용되지않은() throws Exception {
        final FailTransactionWithScheduleDto dto = new FailTransactionWithScheduleDto(1L, now, "event", transactionAmount, "유희왕 카드", "memo");
        callApiForBadRequestWhenCreate(dto);
      }

      @Test
      void 내역_거래_종류_허용되지않은() throws Exception {
        transactionAmount = new FailTransactionAmountDto("빈봉투 주기", 1000L);
        final FailTransactionWithScheduleDto dto = new FailTransactionWithScheduleDto(1L, now, "event", transactionAmount, category, "memo");
        callApiForBadRequestWhenCreate(dto);
      }

      @Test
      void 내역_거래_금액_음수() throws Exception {
        transactionAmount = new FailTransactionAmountDto(action, -1000L);
        final FailTransactionWithScheduleDto dto = new FailTransactionWithScheduleDto(1L, now, "event", transactionAmount, category, "memo");
        callApiForBadRequestWhenCreate(dto);
      }

      @Test
      void 내역_거래_금액_초과() throws Exception {
        transactionAmount = new FailTransactionAmountDto(action, Long.MAX_VALUE + 1L);
        final FailTransactionWithScheduleDto dto = new FailTransactionWithScheduleDto(1L, now, "event", transactionAmount, category, "memo");
        callApiForBadRequestWhenCreate(dto);
      }

      @Test
      void 빈_요청() throws Exception {
        mvc.perform(post(END_POINT)
            ).andDo(print())
            .andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("내역_수정_실패")
    class 내역_수정_실패 {
      @BeforeEach
      void 초기값() {
        action = TransactionAction.RECEIVING;
        category = TransactionCategory.ETC;
        transactionAmount = new FailTransactionAmountDto(action, 1000L);
      }

      private void callApiForBadRequestWhenUpdate(final FailTransactionDto dto) throws Exception {
        final String json = mapper.writeValueAsString(dto);
        final Long transactionId = 1L;

        mvc.perform(patch(END_POINT + "/" + transactionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
            ).andDo(print())
            .andExpect(status().isBadRequest());
      }

      @Test
      void 내역_대상_아이디_NULL() throws Exception {
        final FailTransactionDto dto = new FailTransactionDto(null, transactionAmount, category, "memo");
        callApiForBadRequestWhenUpdate(dto);
      }

      @Test
      void 내역_금액_종류_NULL() throws Exception {
        transactionAmount = new FailTransactionAmountDto(null, 1000L);
        final FailTransactionDto dto = new FailTransactionDto(1L, transactionAmount, category, "memo");
        callApiForBadRequestWhenUpdate(dto);
      }

      @Test
      void 내역_거래_금액_NULL() throws Exception {
        transactionAmount = new FailTransactionAmountDto(action, null);
        final FailTransactionDto dto = new FailTransactionDto(1L, transactionAmount, category, "memo");
        callApiForBadRequestWhenUpdate(dto);
      }

      @Test
      void 내역_거래_자산_종류_NULL() throws Exception {
        transactionAmount = new FailTransactionAmountDto(null, 1000L);
        final FailTransactionDto dto = new FailTransactionDto(1L, transactionAmount, null, "memo");
        callApiForBadRequestWhenUpdate(dto);
      }

      @Test
      void 내역_메모_200자_초과() throws Exception {
        final FailTransactionDto dto = new FailTransactionDto(1L, transactionAmount, category, "m".repeat(201));
        callApiForBadRequestWhenUpdate(dto);
      }

      @Test
      void 내역_선물_종류_허용되지않은() throws Exception {
        final FailTransactionDto dto = new FailTransactionDto(1L, transactionAmount, "부루마블 지폐", "memo");
        callApiForBadRequestWhenUpdate(dto);
      }

      @Test
      void 내역_거래_종류_허용되지않은() throws Exception {
        transactionAmount = new FailTransactionAmountDto("줫다뺏음", 1000L);
        final FailTransactionDto dto = new FailTransactionDto(1L, transactionAmount, category, "memo");
        callApiForBadRequestWhenUpdate(dto);
      }

      @Test
      void 내역_거래_금액_음수() throws Exception {
        transactionAmount = new FailTransactionAmountDto(action, -1000L);
        final FailTransactionDto dto = new FailTransactionDto(1L, transactionAmount, category, "memo");
        callApiForBadRequestWhenUpdate(dto);
      }

      @Test
      void 내역_거래_초과() throws Exception {
        transactionAmount = new FailTransactionAmountDto(action, Long.MAX_VALUE + 1L);
        final FailTransactionDto dto = new FailTransactionDto(1L, transactionAmount, category, "memo");
        callApiForBadRequestWhenUpdate(dto);
      }

      @Test
      void 존재하지_않는_내역() throws Exception {
        doThrow(new CustomException(CustomErrorCode.NOT_FOUND, "수정할 내역을 찾을 수 없음")).when(transactionWriteService).patchTransactionById(any(RequestUpdateTransactionDto.class), anyLong());
        final TransactionAmountDto transactionAmount = new TransactionAmountDto(action, 1000L);
        final RequestUpdateTransactionDto transactionDto = new RequestUpdateTransactionDto(1L, transactionAmount, category, "memo");
        final String json = mapper.writeValueAsString(transactionDto);
        final Long transactionId = 1L;

        mvc.perform(patch(END_POINT + "/" + transactionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
            ).andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("수정할 내역을 찾을 수 없음"));
      }

      @Test
      void 잘못된_내역_아이디_문자열() throws Exception {
        final TransactionAmountDto transactionAmount = new TransactionAmountDto(action, 1000L);
        final RequestUpdateTransactionDto transactionDto = new RequestUpdateTransactionDto(1L, transactionAmount, category, "memo");
        final String json = mapper.writeValueAsString(transactionDto);

        mvc.perform(patch(END_POINT + "/test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
            ).andDo(print())
            .andExpect(status().isBadRequest());
      }

      @Test
      void 잘못된_내역_아이디_소수() throws Exception {
        final TransactionAmountDto transactionAmount = new TransactionAmountDto(action, 1000L);
        final RequestUpdateTransactionDto transactionDto = new RequestUpdateTransactionDto(1L, transactionAmount, category, "memo");
        final String json = mapper.writeValueAsString(transactionDto);

        mvc.perform(patch(END_POINT + "/0.03")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
            ).andDo(print())
            .andExpect(status().isBadRequest());
      }

      @Test
      void 빈_요청() throws Exception {
        final Long transactionId = 1L;

        mvc.perform(patch(END_POINT + "/" + transactionId)
            ).andDo(print())
            .andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("내역_삭제_실패")
    class 내역_삭제_실패 {
      @Test
      void 존재하지_않는_내역() throws Exception {
        doThrow(new CustomException(CustomErrorCode.NOT_FOUND, "삭제할 내역을 찾을 수 없음")).when(transactionWriteService).removeTransactionById(anyLong());

        final Long transactionId = 1L;

        mvc.perform(delete(END_POINT + "/" + transactionId))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("삭제할 내역을 찾을 수 없음"));
      }

      @Test
      void 잘못된_내역_아이디_문자열() throws Exception {
        mvc.perform(delete(END_POINT + "/1번"))
            .andDo(print())
            .andExpect(status().isBadRequest());
      }

      @Test
      void 잘못된_내역_아이디_소수() throws Exception {
        mvc.perform(delete(END_POINT + "/0.3"))
            .andDo(print())
            .andExpect(status().isBadRequest());
      }
    }
  }
}

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@RequiredArgsConstructor
class FailTransactionWithScheduleDto {
  private final Object personId;
  private final Object eventDate;
  private final Object event;
  private final FailTransactionAmountDto transactionAmount;
  private final Object category;
  private final Object memo;
}

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@RequiredArgsConstructor
class FailTransactionDto {
  private final Object personId;
  private final FailTransactionAmountDto transactionAmount;
  private final Object category;
  private final Object memo;
}

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@RequiredArgsConstructor
class FailTransactionAmountDto {
  private final Object action;
  private final Object amount;
}
