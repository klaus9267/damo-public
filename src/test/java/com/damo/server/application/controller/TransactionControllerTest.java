package com.damo.server.application.controller;

import com.damo.server.common.WithMockCustomUser;
import com.damo.server.domain.transaction.TransactionRepository;
import com.damo.server.domain.transaction.dto.RequestCreateTransactionDto;
import com.damo.server.domain.transaction.dto.RequestUpdateTransactionDto;
import com.damo.server.domain.transaction.entity.Transaction;
import com.damo.server.domain.transaction.entity.TransactionAction;
import com.damo.server.domain.transaction.entity.TransactionAmount;
import com.damo.server.domain.transaction.entity.TransactionCategory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TransactionControllerTest {
  private final String END_POINT = "/api/transactions";
  @Autowired
  MockMvc mvc;
  @MockBean
  TransactionRepository transactionRepository;
  
  @Nested
  @DisplayName("성공 케이스")
  @WithMockCustomUser
  class 성공 {
    LocalDateTime now;
    
    @BeforeEach
    void 초기값() {
      now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
      when(transactionRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(new Transaction()));
    }
    
    @Test
    void 내역_생성() throws Exception {
      final TransactionAction action = TransactionAction.RECEIVING;
      final TransactionAmount amount = new TransactionAmount(action, 1000L);
      final TransactionCategory category = TransactionCategory.ETC;
      final RequestCreateTransactionDto createTransactionDto = new RequestCreateTransactionDto(1L, now, "테스트 이벤트", amount, category, "테스트 메모");
      
      final ObjectMapper mapper = new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
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
      final TransactionAction action = TransactionAction.RECEIVING;
      final TransactionAmount amount = new TransactionAmount(action, 1000L);
      final TransactionCategory category = TransactionCategory.ETC;
      final RequestUpdateTransactionDto updateTransactionDto = new RequestUpdateTransactionDto(1L, amount, category, "테스트 메모");
      final ObjectMapper mapper = new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
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
    
    @Test
    void 내역_생성() throws Exception {
      final TransactionAction action = TransactionAction.RECEIVING;
      final TransactionAmount amount = new TransactionAmount(action, 1000L);
      final TransactionCategory category = TransactionCategory.ETC;
      RequestCreateTransactionDto createTransactionDto = new RequestCreateTransactionDto(null, now, "테스트 이벤트", amount, category, "테스트 메모");
      
      ObjectMapper mapper = new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      String json = mapper.writeValueAsString(createTransactionDto);
      
      // 대상 아이디 실패
      mvc.perform(post(END_POINT)
             .contentType(MediaType.APPLICATION_JSON)
             .content(json)
             .accept(MediaType.APPLICATION_JSON)
         ).andDo(print())
         .andExpect(status().isBadRequest())
         .andDo(print());
      
      createTransactionDto = new RequestCreateTransactionDto(1L, null, "테스트 이벤트", amount, category, "테스트 메모");
      
      mapper = new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      json = mapper.writeValueAsString(createTransactionDto);
      
      // 거래 날짜 null
      mvc.perform(post(END_POINT)
             .contentType(MediaType.APPLICATION_JSON)
             .content(json)
             .accept(MediaType.APPLICATION_JSON)
         ).andDo(print())
         .andExpect(status().isBadRequest())
         .andDo(print());
      
      createTransactionDto = new RequestCreateTransactionDto(1L, now, "테스트 이벤트", amount, null, "테스트 메모");
      
      mapper = new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      json = mapper.writeValueAsString(createTransactionDto);
      
      // 거래 종류 실패
      mvc.perform(post(END_POINT)
             .contentType(MediaType.APPLICATION_JSON)
             .content(json)
             .accept(MediaType.APPLICATION_JSON)
         ).andDo(print())
         .andExpect(status().isBadRequest())
         .andDo(print());
      
      createTransactionDto = new RequestCreateTransactionDto(1L, now, null, amount, category, "테스트 메모");
      
      mapper = new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      json = mapper.writeValueAsString(createTransactionDto);
      
      // 행사 이름 null
      mvc.perform(post(END_POINT)
             .contentType(MediaType.APPLICATION_JSON)
             .content(json)
             .accept(MediaType.APPLICATION_JSON)
         ).andDo(print())
         .andExpect(status().isBadRequest())
         .andDo(print());
      
      createTransactionDto = new RequestCreateTransactionDto(1L, now, null, amount, category, "테스트 메모");
      
      when(transactionRepository.existsByEventDateAndPersonIdAndEvent(now, 1L, "테스트 이벤트")).thenReturn(true);
      
      mapper = new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      json = mapper.writeValueAsString(createTransactionDto);
      
      // 데이터 중복 실패
      mvc.perform(post(END_POINT)
             .contentType(MediaType.APPLICATION_JSON)
             .content(json)
             .accept(MediaType.APPLICATION_JSON)
         ).andDo(print())
         .andExpect(status().isBadRequest())
         .andDo(print());
    }
    
    @Test
    void 내역_수정() throws Exception {
      final TransactionAction action = TransactionAction.RECEIVING;
      final TransactionAmount amount = new TransactionAmount(action, 1000L);
      final TransactionCategory category = TransactionCategory.ETC;
      RequestUpdateTransactionDto transactionDto = new RequestUpdateTransactionDto(null, amount, category, "테스트 메모");
      
      ObjectMapper mapper = new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      String json = mapper.writeValueAsString(transactionDto);
      
      // 대상 아이디 실패
      mvc.perform(patch(END_POINT + "/" + 1L)
             .contentType(MediaType.APPLICATION_JSON)
             .content(json)
             .accept(MediaType.APPLICATION_JSON)
         ).andDo(print())
         .andExpect(status().isBadRequest())
         .andDo(print());
      
      transactionDto = new RequestUpdateTransactionDto(1L, null, category, "테스트 메모");
      
      mapper = new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      json = mapper.writeValueAsString(transactionDto);
      
      // 거래 금액 실패
      mvc.perform(patch(END_POINT + "/" + 1L)
             .contentType(MediaType.APPLICATION_JSON)
             .content(json)
             .accept(MediaType.APPLICATION_JSON)
         ).andDo(print())
         .andExpect(status().isBadRequest())
         .andDo(print());
      
      transactionDto = new RequestUpdateTransactionDto(1L, amount, category, "테스트 메모");
      
      mapper = new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      json = mapper.writeValueAsString(transactionDto);
      
      when(transactionRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());
      
      // 내역 아이디 실패
      mvc.perform(patch(END_POINT + "/" + 1L)
             .contentType(MediaType.APPLICATION_JSON)
             .content(json)
             .accept(MediaType.APPLICATION_JSON)
         ).andDo(print())
         .andExpect(status().isNotFound())
         .andDo(print());
    }
    
    @Test
    void 내역_삭제() throws Exception {
      // 내역 아이디 실패
      mvc.perform(delete(END_POINT + "/" + "test")
             .contentType(MediaType.APPLICATION_JSON)
             .accept(MediaType.APPLICATION_JSON)
         ).andDo(print())
         .andExpect(status().isBadRequest())
         .andDo(print());
      
      when(transactionRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());
      // 내역 아이디 실패
      mvc.perform(delete(END_POINT + "/" + 0L)
             .contentType(MediaType.APPLICATION_JSON)
             .accept(MediaType.APPLICATION_JSON)
         ).andDo(print())
         .andExpect(status().isNotFound())
         .andDo(print());
    }
  }
}