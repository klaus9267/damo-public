package com.damo.server.application.controller;

import com.damo.server.application.handler.exception.BadRequestException;
import com.damo.server.domain.person.PersonService;
import com.damo.server.domain.person.dto.PersonDto;
import com.damo.server.domain.person.dto.RequestPersonDto;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
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
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PersonControllerTest {
    @MockBean
    PersonService personService;

    @Autowired
    MockMvc mvc;

    private final String END_POINT = "/api/persons";

    @Nested
    @DisplayName("성공 케이스")
    class 성공 {
        Timestamp now;
        @BeforeEach
        void 초기값() {
            now = Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        }

        @Test
        void 대상_생성() throws Exception {
            PersonDto personDto = new PersonDto(1L, "테스트 이름", "가족", "테스트 메모", now, now);
            given(personService.save(any())).willReturn(personDto);

            RequestPersonDto requestPersonDto = new RequestPersonDto(personDto.getName(), personDto.getRelation(), personDto.getMemo(), personDto.getId());
            ObjectMapper mapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            String json = mapper.writeValueAsString(requestPersonDto);

            mvc.perform(post(END_POINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.relation").value(requestPersonDto.relation()))
                    .andExpect(jsonPath("$.memo").value(requestPersonDto.memo()))
                    .andExpect(jsonPath("$.name").value(requestPersonDto.name()));
        }

        @Test
        void 대상_수정() throws Exception {
            PersonDto personDto = new PersonDto(1L, "테스트 이름", "가족", "테스트 메모", now, now);
            given(personService.patchPersonById(any(), any(Long.class))).willReturn(personDto);

            RequestPersonDto requestPersonDto = new RequestPersonDto(personDto.getName(), personDto.getRelation(), personDto.getMemo(), 1L);
            ObjectMapper mapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            String json = mapper.writeValueAsString(requestPersonDto);

            mvc.perform(patch(END_POINT + "/" + personDto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.relation").value(requestPersonDto.relation()))
                    .andExpect(jsonPath("$.memo").value(requestPersonDto.memo()))
                    .andExpect(jsonPath("$.name").value(requestPersonDto.name()));
        }

        @Test
        void 대상_삭제() throws Exception {
            RequestPersonDto requestPersonDto = new RequestPersonDto("테스트 이름", "가족", "테스트 메모", 1L);
            ObjectMapper mapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            String json = mapper.writeValueAsString(requestPersonDto);

            mvc.perform(delete(END_POINT + "/" + 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(print())
                    .andExpect(status().isNoContent());
        }
    }
    @Nested
    @DisplayName("실패 케이스")
    class 실패 {
        Timestamp now;
        @BeforeEach
        void 초기값() {
            now = Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        }


        @Test
        void 대상_생성_실패() throws Exception {

            FailPersonDto personDto = new FailPersonDto(null, "가족", null, 1L);
            ObjectMapper mapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            String json = mapper.writeValueAsString(personDto);

            // 이름 실패
            mvc.perform(post(END_POINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(print());

            personDto = new FailPersonDto("이름", null, null, 1L);
            mapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            json = mapper.writeValueAsString(personDto);

            // 관계 실패
            mvc.perform(post(END_POINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(print());


            personDto = new FailPersonDto("이름", "가족", 123, 1L);
            mapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            json = mapper.writeValueAsString(personDto);

            // 메모 실패
            mvc.perform(post(END_POINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(print());


            personDto = new FailPersonDto("이름", "가족", "112233", 0L);
            mapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            json = mapper.writeValueAsString(personDto);

            // 유저 아이디 실패
            mvc.perform(post(END_POINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }
    }
}

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class FailPersonDto {
    private final Object name;
    private final Object relation;
    private final Object memo;
    private final Object userId;
    public FailPersonDto(Object name, Object relation, Object memo, Object userId) {
        this.name = name;
        this.relation = relation;
        this.memo = memo;
        this.userId = userId;
    }
}