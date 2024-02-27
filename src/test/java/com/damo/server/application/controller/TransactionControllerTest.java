package com.damo.server.application.controller;

import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import com.damo.server.common.WithMockCustomUser;
import com.damo.server.domain.transaction.dto.RequestCreateTransactionDto;
import com.damo.server.domain.transaction.dto.RequestUpdateTransactionDto;
import com.damo.server.domain.transaction.entity.TransactionAction;
import com.damo.server.domain.transaction.entity.TransactionAmount;
import com.damo.server.domain.transaction.entity.TransactionCategory;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
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
  
  @Nested
  @DisplayName("성공 케이스")
  @WithMockCustomUser
  class 성공 {
    final TransactionAction action = TransactionAction.RECEIVING;
    final TransactionAmount amount = new TransactionAmount(action, 1000L);
    final TransactionCategory category = TransactionCategory.ETC;
    LocalDateTime now;
    
    @BeforeEach
    void 초기값() {
      now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }
    
    @Test
    void 내역_생성() throws Exception {
      final RequestCreateTransactionDto createTransactionDto = new RequestCreateTransactionDto(1L, now, "테스트 이벤트", amount, category, "테스트 메모");
      final String json = mapper.writeValueAsString(createTransactionDto);
      
      mvc.perform(post(END_POINT)
             .contentType(MediaType.APPLICATION_JSON)
             .content(json)
             .accept(MediaType.APPLICATION_JSON)
         ).andDo(print())
         .andExpect(status().isCreated())
         .andExpect(jsonPath("$").doesNotExist());
    }
    
    @Test
    void 내역_수정() throws Exception {
      final RequestUpdateTransactionDto updateTransactionDto = new RequestUpdateTransactionDto(1L, amount, category, "테스트 메모");
      final String json = mapper.writeValueAsString(updateTransactionDto);
      
      mvc.perform(patch(END_POINT + "/" + 1)
             .contentType(MediaType.APPLICATION_JSON)
             .content(json)
             .accept(MediaType.APPLICATION_JSON)
         ).andDo(print())
         .andExpect(status().isNoContent())
         .andExpect(jsonPath("$").doesNotExist());
    }
    
    @Test
    void 내역_삭제() throws Exception {
      mvc.perform(delete(END_POINT + "/" + 1L)
             .contentType(MediaType.APPLICATION_JSON)
             .accept(MediaType.APPLICATION_JSON)
         ).andDo(print())
         .andExpect(status().isNoContent())
         .andExpect(jsonPath("$").doesNotExist());
    }
  }
  
  @Nested
  @DisplayName("실패 케이스")
  @WithMockCustomUser
  class 실패 {
    LocalDateTime now;
    
    @BeforeEach
    void 초기값() {
      now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }
    
    @Nested
    @DisplayName("내역_생성_실패")
    class 내역_생성_실패 {
      TransactionAction action = TransactionAction.RECEIVING;
      TransactionCategory category = TransactionCategory.ETC;
      TransactionAmount amount = new TransactionAmount(action, 1000L);
      
      private void callApiForBadRequestWhenCreate(final FailTransactionWithScheduleDto dto) throws Exception {
        final String json = mapper.writeValueAsString(dto);
        
        mvc.perform(post(END_POINT)
               .contentType(MediaType.APPLICATION_JSON)
               .content(json)
           )
           .andDo(print())
           .andExpect(status().isBadRequest());
      }
      
      @Test
      void 내역_대상_아이디_NULL() throws Exception {
        final FailTransactionWithScheduleDto dto = new FailTransactionWithScheduleDto(null, now, "event", amount, category, "memo");
        callApiForBadRequestWhenCreate(dto);
      }
      
      @Test
      void 내역_날짜_NULL() throws Exception {
        final FailTransactionWithScheduleDto dto = new FailTransactionWithScheduleDto(1L, null, "event", amount, category, "memo");
        callApiForBadRequestWhenCreate(dto);
      }
      
      @Test
      void 내역_금액_객체_NULL() throws Exception {
        final FailTransactionWithScheduleDto dto = new FailTransactionWithScheduleDto(1L, now, "event", amount, null, "memo");
        callApiForBadRequestWhenCreate(dto);
      }
      
      @Test
      void 내역_행사_이름_NULL() throws Exception {
        final FailTransactionWithScheduleDto dto = new FailTransactionWithScheduleDto(1L, now, null, amount, category, "memo");
        callApiForBadRequestWhenCreate(dto);
      }
      
      @Test
      void 내역_행사_이름_빈문자열() throws Exception {
        final FailTransactionWithScheduleDto dto = new FailTransactionWithScheduleDto(1L, now, "", amount, category, "memo");
        callApiForBadRequestWhenCreate(dto);
      }
      
      @Test
      void 내역_거래_종류_NULL() throws Exception {
        amount = TransactionAmount.builder().action(null).amount(1000L).build();
        final FailTransactionWithScheduleDto dto = new FailTransactionWithScheduleDto(1L, now, null, amount, category, "memo");
        callApiForBadRequestWhenCreate(dto);
      }
      
      @Test
      void 내역_메모_200자_초과() throws Exception {
        amount = TransactionAmount.builder().action(null).amount(1000L).build();
        final FailTransactionWithScheduleDto dto = new FailTransactionWithScheduleDto(1L, now, null, amount, category, "m".repeat(201));
        callApiForBadRequestWhenCreate(dto);
      }
      
      @Test
      void 내역_선물_종류_허용되지않은() throws Exception {
        final FailTransactionWithScheduleDto dto = new FailTransactionWithScheduleDto(1L, now, null, amount, "유희왕 카드", "m".repeat(201));
        callApiForBadRequestWhenCreate(dto);
      }
      
      @Test
      void 내역_거래_종류_허용되지않은() throws Exception {
        final FailTransactionWithScheduleDto dto = new FailTransactionWithScheduleDto(1L, now, null, "북한 돈", category, "m".repeat(201));
        callApiForBadRequestWhenCreate(dto);
      }
    }
    
    @Nested
    @DisplayName("내역_수정_실패")
    class 내역_수정_실패 {
      TransactionAction action = TransactionAction.RECEIVING;
      TransactionCategory category = TransactionCategory.ETC;
      TransactionAmount amount = new TransactionAmount(action, 1000L);
      
      private void callApiForBadRequestWhenUpdate(final FailTransactionDto dto) throws Exception {
        final String json = mapper.writeValueAsString(dto);
        final Long transactionId = 1L;
        
        mvc.perform(patch(END_POINT + "/" + transactionId)
               .contentType(MediaType.APPLICATION_JSON)
               .content(json)
           )
           .andDo(print())
           .andExpect(status().isBadRequest());
      }
      
      @Test
      void 내역_대상_아이디_NULL() throws Exception {
        final FailTransactionDto dto = new FailTransactionDto(null, amount, category, "memo");
        callApiForBadRequestWhenUpdate(dto);
      }
      
      @Test
      void 내역_금액_객체_NULL() throws Exception {
        final FailTransactionDto dto = new FailTransactionDto("event", amount, null, "memo");
        callApiForBadRequestWhenUpdate(dto);
      }
      
      @Test
      void 내역_거래_종류_NULL() throws Exception {
        amount = TransactionAmount.builder().action(null).amount(1000L).build();
        final FailTransactionDto dto = new FailTransactionDto(null, amount, category, "memo");
        callApiForBadRequestWhenUpdate(dto);
      }
      
      @Test
      void 내역_메모_200자_초과() throws Exception {
        amount = TransactionAmount.builder().action(null).amount(1000L).build();
        final FailTransactionDto dto = new FailTransactionDto(null, amount, category, "m".repeat(201));
        callApiForBadRequestWhenUpdate(dto);
      }
      
      @Test
      void 내역_선물_종류_허용되지않은() throws Exception {
        final FailTransactionDto dto = new FailTransactionDto(null, amount, "뇌물", "m".repeat(201));
        callApiForBadRequestWhenUpdate(dto);
      }
      
      @Test
      void 내역_거래_종류_허용되지않은() throws Exception {
        final FailTransactionDto dto = new FailTransactionDto(null, "부루마블 지폐", category, "m".repeat(201));
        callApiForBadRequestWhenUpdate(dto);
      }
      
      @Test
      void 존재하지_않는_내역() throws Exception {
        doThrow(new CustomException(CustomErrorCode.NOT_FOUND, "수정할 내역을 찾을 수 없음"))
            .when(transactionWriteService)
            .patchTransactionById(any(RequestUpdateTransactionDto.class), anyLong());
        final RequestUpdateTransactionDto transactionDto = new RequestUpdateTransactionDto(1L, amount, category, "memo");
        final String json = mapper.writeValueAsString(transactionDto);
        final Long transactionId = 1L;
        
        mvc.perform(patch(END_POINT + "/" + transactionId)
               .contentType(MediaType.APPLICATION_JSON)
               .content(json)
           )
           .andDo(print())
           .andExpect(status().isNotFound())
           .andExpect(jsonPath("$.message").value("수정할 내역을 찾을 수 없음"));
      }
    }
    
    @Nested
    @DisplayName("내역_삭제_실패")
    class 내역_삭제_실패 {
      @Test
      void 존재하지_않는_내역() throws Exception {
        doThrow(new CustomException(CustomErrorCode.NOT_FOUND, "삭제할 내역을 찾을 수 없음"))
            .when(transactionWriteService)
            .removeTransactionById(anyLong());
        final Long transactionId = 1L;
        
        mvc.perform(delete(END_POINT + "/" + transactionId))
           .andDo(print())
           .andExpect(status().isNotFound())
           .andExpect(jsonPath("$.message").value("삭제할 내역을 찾을 수 없음"));
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
  private final Object transactionAmount;
  private final Object category;
  private final Object memo;
}

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@RequiredArgsConstructor
class FailTransactionDto {
  private final Object personId;
  private final Object transactionAmount;
  private final Object category;
  private final Object memo;
}
